package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import android.os.Build
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.utils.MaskUtils
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.InitActionIcon

@Composable
internal fun CoreEditText(
    isError: Boolean,
    placeholder: String,
    mask: String?,
    regex: Regex?,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    text: MutableState<TextFieldValue>,
    hasFocus: MutableState<Boolean>,
    focusRequester: FocusRequester,
    actionOnTextChanged: (String) -> Unit,
    actionClickInfo: (() -> Unit)?,
    paySystemIcon: Int? = null,
    visualTransformation: VisualTransformation? = null,
    isDateExpiredMask: Boolean = false
) {

    val maskUtils: MaskUtils? =
        if (mask == null || Build.VERSION.SDK_INT < Build.VERSION_CODES.Q) null
        else MaskUtils(mask, isDateExpiredMask)

    val onTextChanged: ((TextFieldValue) -> Unit) = {
        if (maskUtils != null
            && it.text.length > text.value.text.length
        ) {
            val result = if (regex != null)
                clearText(
                    text = it.text,
                    regex = regex
                ) else it.text

            text.value = TextFieldValue(
                text = maskUtils.format(result),
                selection = TextRange(maskUtils.getNextCursorPosition(it.selection.end))
            )

        } else {
            text.value = it
        }

        actionOnTextChanged.invoke(text.value.text)
    }

    TextField(
        value = text.value,
        onValueChange = onTextChanged,
        label = { Text(text = placeholder) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
        visualTransformation = visualTransformation ?: VisualTransformation.None,
        colors = TextFieldDefaults.outlinedTextFieldColors(
            backgroundColor = if (isError) ColorsSdk.stateBgError else ColorsSdk.bgBlock,
            textColor = if (isError) ColorsSdk.stateError else ColorsSdk.textMain,
            focusedLabelColor = if (isError) ColorsSdk.stateError else ColorsSdk.colorBrandMainMS.value,
            unfocusedLabelColor = if (isError) ColorsSdk.stateError else ColorsSdk.textLight,
            focusedBorderColor = ColorsSdk.transparent,
            cursorColor = ColorsSdk.colorBrandMainMS.value,
            unfocusedBorderColor = ColorsSdk.transparent,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onFocusChanged {
                hasFocus.value = it.hasFocus
            }
            .focusRequester(focusRequester),
        leadingIcon = if (actionClickInfo == null
            && paySystemIcon == null
       ) {
            null
        } else if(paySystemIcon != null) {{
            InitIconPaySystem(
                isError = isError,
                text = text.value.text,
                paySystemIcon = paySystemIcon
            )

        }} else {{
            InitIconInfo(
                isError = isError,
                actionClickInfo = { actionClickInfo?.invoke() }
            )
        }}
    )
}

private fun clearText(
    text: String,
    regex: Regex?
) = try {
    val temp = regex?.let { regexForClear ->
        text.replace(
            regex = regexForClear,
            replacement = ""
        )
    } ?: text

    temp

} catch (e: Exception) {
//    e.printStackTrace()
    ""
}

@Composable
private fun InitIconInfo(
    isError: Boolean,
    actionClickInfo: () -> Unit
) {
    InitActionIcon(
        action = actionClickInfo,
        iconSrc = R.drawable.hint,
        modifier = Modifier.size(40.dp),
        _outlinedButtonColor = if (isError) ColorsSdk.stateBgError else ColorsSdk.bgBlock
    )
}

@Composable
private fun InitIconPaySystem(
    isError: Boolean,
    text: String,
    paySystemIcon: Int?
) {
    if (
        text.isNotBlank()
        && paySystemIcon != null
    ) {
        InitActionIcon(
            action = null,
            iconSrc = paySystemIcon,
            modifier = Modifier.size(40.dp),
            _outlinedButtonColor = if (isError) ColorsSdk.stateBgError else ColorsSdk.bgBlock
        )
    }
}
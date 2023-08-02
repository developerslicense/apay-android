package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import kz.airbapay.apay_android.data.utils.MaskUtils
import kz.airbapay.apay_android.data.utils.messageLog
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun CoreEditText(
    placeholder: String,
    mask: String?,
    regex: Regex?,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    text: MutableState<TextFieldValue>,
    hasFocus: MutableState<Boolean>,
    focusRequester: FocusRequester,
    actionOnTextChanged: (String) -> Unit,
    visualTransformation: VisualTransformation? = null,
    isDateExpiredMask: Boolean = false
) {

    val maskUtils: MaskUtils? = if (mask == null) null else MaskUtils(mask, isDateExpiredMask)

    var cursorPosition by remember {
        mutableStateOf(0)
    }

    val onTextChanged: ((TextFieldValue) -> Unit) = {
        if(it.text.length > text.value.text.length) {
            val result = if (regex != null)
                clearText(
                    text = it.text,
                    regex = regex
                ) else it.text

            cursorPosition = maskUtils?.getNextCursorPosition(it.selection.end) ?: 0
            text.value = TextFieldValue(
                text = maskUtils?.format(result) ?: result,
                selection = TextRange(cursorPosition)
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
            backgroundColor = ColorsSdk.bgBlock,
            textColor = ColorsSdk.textMain,
            focusedLabelColor = ColorsSdk.colorBrandMainMS.value,
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
    e.printStackTrace()
    ""
}

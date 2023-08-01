package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.layout.fillMaxWidth
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
import androidx.compose.ui.text.input.VisualTransformation
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun CoreEditText(
    placeholder: String,
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions,
    text: MutableState<String>,
    hasFocus: MutableState<Boolean>,
    focusRequester: FocusRequester,
    visualTransformation: VisualTransformation,
) {

  /*  val actionTextChanged: ((String) -> Unit) = {
        val result = clearText(
            text = it,
            viewModel = viewModel
        )

        text.value = if (content.value.textLengthLimit != null) {
            result.take(content.value.textLengthLimit ?: 0)
        } else {
            result
        }

        content.value.actionTextChanged?.invoke(text.value)
    }*/

    TextField(
        value = text.value,
        onValueChange = {
            text.value = it
        },
        label = { Text(text = placeholder) },
        keyboardOptions = keyboardOptions,
        keyboardActions = keyboardActions,
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
//            visualTransformation = visualTransformation,
    )
}

private fun EditTextViewModel.clearText(
    text: String
) = try {
    val temp = content.value.regexForClear?.let { regexForClear ->
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

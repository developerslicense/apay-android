package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.material.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.text.input.VisualTransformation
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun CoreEditText(
    visualTransformation: VisualTransformation,
    viewModel: EditTextViewModel
) {

    with(viewModel) {
        val actionTextChanged: ((String) -> Unit) = {
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
        }

        TextField(
            value = text.value,
            onValueChange = actionTextChanged,
            label = { Text(text = viewModel.content.value.getPlaceholderText() ?: "") },
            keyboardOptions = viewModel.content.value.keyboardOptions,
            keyboardActions = viewModel.content.value.keyboardActions,
            colors =  TextFieldDefaults.outlinedTextFieldColors(
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
                    viewModel.setStateFocused(it.hasFocus)
                }
                .focusRequester(viewModel.focusRequester),
            visualTransformation = visualTransformation,
        )
    }
}

private fun EditTextViewModel.clearText(
    text: String,
    viewModel: EditTextViewModel
) = try {
    val temp = content.value.regexForClear?.let { regexForClear ->
        text.replace(
            regex = regexForClear,
            replacement = ""
        )
    } ?: text

    if (viewModel.content.value.isMoney) {
        temp
            .toLong() // это нужно, чтоб в случае ввода "00000", в "0" трансформировалось
            .toString()

    } else {
        temp
    }

} catch (e: Exception) {
    e.printStackTrace()
    ""
}

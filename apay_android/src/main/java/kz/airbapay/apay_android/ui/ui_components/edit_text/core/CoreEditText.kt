package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.input.VisualTransformation
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun CoreEditText(
    modifier: Modifier,
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

        BasicTextField(
            modifier = modifier
                .background(backgroundColor.value)
                .onFocusChanged {
                    viewModel.setStateFocused(it.hasFocus)
                    backgroundColor.value = content.value.editTextFieldBackgroundDefault
                }
                .focusRequester(viewModel.focusRequester)
                .fillMaxWidth(),
            value = text.value,
            keyboardOptions = content.value.keyboardOptions,
            keyboardActions = KeyboardActions(onDone = {
//                FragmentHelper.hideKeyboard()
                content.value.keyboardActions?.invoke()
            }),
            visualTransformation = visualTransformation,
            onValueChange = actionTextChanged,
            singleLine = content.value.maxLines == 1,
            maxLines = content.value.maxLines,
            cursorBrush = SolidColor(ColorsSdk.buttonMainBrandMS.value),
            textStyle = LocalFonts.current.bodyRegular,
            decorationBox = { innerTextField ->
                innerTextField()
            }
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

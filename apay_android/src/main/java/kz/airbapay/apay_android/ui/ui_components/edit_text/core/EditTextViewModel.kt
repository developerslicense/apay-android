package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.TextFieldValue
import androidx.lifecycle.ViewModel
import kz.airbapay.apay_android.data.constant.RegexConst.DIGITS
import kz.airbapay.apay_android.data.model.EditTextDVO
import kz.airbapay.apay_android.data.utils.MaskFormatter
import kz.airbapay.apay_android.ui.resources.ColorsSdk

/**
 * Если вставляешь программно текст, то в textFieldValueState его добавь тоже (при условии, что маска используется)
 * */

internal class EditTextViewModel(
    _content: EditTextDVO,
    val text: MutableState<String> = mutableStateOf(""),
    val textFieldValueState: MutableState<TextFieldValue> = mutableStateOf(TextFieldValue(text = "")), //todo временно
    val hasFocus: MutableState<Boolean> = mutableStateOf(false),
    val focusRequester: FocusRequester = FocusRequester(),
) : ViewModel() {
    private val onlyDigits = Regex(DIGITS)
    val maskUtils = MaskFormatter(_content.mask ?: "")

    val content = mutableStateOf(_content)
    val backgroundColor = mutableStateOf(content.value.editTextFieldBackgroundDefault)
    val borderColor = mutableStateOf(getEditTextStateBorder(content.value))

    fun setStateError(isError: Boolean) {
        content.value = content.value.copy(stateError = isError)
        borderColor.value = getEditTextStateBorder(content.value)
    }

    fun setStateFocused(isFocused: Boolean) {
        if (content.value.stateSearchIconEnd
            && isFocused
        ) {
            content.value = content.value.copy(stateSearchIconEnd = false)
        }
        hasFocus.value = isFocused
        content.value = content.value.copy(stateFocused = isFocused)
        borderColor.value = getEditTextStateBorder(content.value)
    }

    /**
     * в 1.3.~ композе краш есть при клике на некоторые места поля ввода, если с ренджем не совпадет.
     * во всех версиях. поэтому, пока влепил костыль. в дальнейшем, эти строчки надо будет удалить
     * */
    // todo попробуй переделать на норм решение летом 2023

    val cursorPosition = mutableStateOf(0) //todo временное решение
    val clearedMask = _content.mask //todo временное решение
        ?.replace("[", "")
        ?.replace("]", "")

    val mapOfCursorPositions = HashMap<Int, Int>().apply { //todo временное решение
        var cursorPosition = 0
        clearedMask?.forEachIndexed { index, c ->
            put(index, cursorPosition)

            if (c.toString().matches(onlyDigits)) {
                cursorPosition++
            }
        }
    }

    private fun getEditTextStateBorder(
        content: EditTextDVO
    ) = when {
//        !content.stateEnabled -> olorsSdk.gray5
        content.stateError && content.needShowError -> ColorsSdk.stateError
        content.stateFocused -> ColorsSdk.buttonMainBrandMS.value
        else -> ColorsSdk.gray20
    }
}
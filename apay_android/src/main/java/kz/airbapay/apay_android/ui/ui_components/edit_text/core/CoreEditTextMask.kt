package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.RegexConst.DIGITS
import kz.airbapay.apay_android.data.constant.RegexConst.NOT_DIGITS
import kz.airbapay.apay_android.data.utils.messageLog
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

//todo это временный костыль, из-за того, что в композ 1.3.~ краш происходит при клике в VisualTransformation

@Composable
internal fun CoreEditTextMask(
    modifier: Modifier,
    viewModel: EditTextViewModel
) {
    val notDigits = Regex(NOT_DIGITS)
    val keyboard = LocalSoftwareKeyboardController.current

    var isCursorPositionEnd by remember {
        mutableStateOf(true)
    }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .height(250.dp)
            .clickable {
                viewModel.focusRequester.requestFocus()
            }
    ) {
        with(viewModel) {

            OutlinedTextField(
                modifier = modifier
                    .background(backgroundColor.value)
                    .onFocusChanged {
                        viewModel.setStateFocused(it.hasFocus)
                        backgroundColor.value = content.value.editTextFieldBackgroundDefault
                    }
                    .focusRequester(focusRequester)
                    .fillMaxWidth()
                    .height(0.dp),
                value = textFieldValueState.value,
                onValueChange = {

                    text.value = maskUtils.format(it.text)
                    messageLog("onValueChange ${it.selection.start} || ${it.selection.end} || ${it.text} || ${text.value}")
                    cursorPosition.value = it.selection.end
                    content.value.actionTextChanged?.invoke(text.value)
//                    content.value.actionCheckValid?.invoke(maskUtils.valueValid)

                    val result = content.value.regexForClear?.let { regexForClear ->
                        it.text.replace(regexForClear, "")
                    } ?: it.text

                    textFieldValueState.value = it.copy(
                        text = if (content.value.textLengthLimit != null) {
                            result
                                .replace(notDigits, "")
                                .take(content.value.textLengthLimit ?: 0)
                        } else {
                            result
                        }
                    )
                },
                keyboardOptions = content.value.keyboardOptions,
                keyboardActions = KeyboardActions(onDone = {
//                    FragmentHelper.hideKeyboard()
                    content.value.keyboardActions?.invoke()
                }),
                singleLine = content.value.maxLines == 1,
                maxLines = content.value.maxLines,
                textStyle = LocalFonts.current.bodyRegular,
            )

            val onlyDigits = Regex(DIGITS)
            val infiniteTransition = rememberInfiniteTransition(
                label = "rememberInfiniteTransition CoreEditTextMask"
            )

            val flash by infiniteTransition.animateFloat(
                initialValue = 1f,
                targetValue = 0f,
                animationSpec = infiniteRepeatable(
                    animation = tween(
                        durationMillis = 1000,
                        easing = FastOutSlowInEasing
                    ),
                    repeatMode = RepeatMode.Restart
                ),
                label = "animateFloat CoreEditTextMask"
            )

            Box(
                modifier = Modifier
                    .background(backgroundColor.value)
                    .fillMaxWidth()
                    .height(100.dp)
                    .clickable {
                        viewModel.focusRequester.requestFocus()
                        keyboard?.show()
                    }
            ) {
                Row(
                    modifier = Modifier
                        .background(backgroundColor.value)
                        .fillMaxWidth()
                        .padding(vertical = 14.dp)
                        .padding(start = 10.dp)
                ) {
                    for (i in 0..(clearedMask?.length ?: 0)) {

                        Box {
                            if (hasFocus.value
                                && !isCursorPositionEnd
                                && cursorPosition.value == (mapOfCursorPositions[i] ?: 0)
                                && text.value.isNotEmpty()
                                && text.value.length > i
                                && text.value[i].toString().matches(onlyDigits)
                            ) {
                                Box(
                                    modifier = Modifier
                                        .height(25.dp)
                                        .width(2.dp)
                                        .background(
                                            ColorsSdk.mainBrand.copy(flash)
                                        )
                                )
                            }

                            if (i < text.value.length) {
                                Text(
                                    style = LocalFonts.current.bodyRegular,
                                    text = text.value[i].toString(),
                                    modifier = Modifier.clickable {
                                        viewModel.focusRequester.requestFocus()
                                        keyboard?.show()
                                        isCursorPositionEnd = false
                                        cursorPosition.value = mapOfCursorPositions[i] ?: 0

                                        textFieldValueState.value = TextFieldValue(
                                            text = textFieldValueState.value.text,
                                            selection = TextRange(mapOfCursorPositions[i] ?: 0)
                                        )
                                    }
                                )
                            }
                        }
                    }

                    if (isCursorPositionEnd
                        && hasFocus.value
                    ) {
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .width(2.dp)
                                .background(
                                    ColorsSdk.mainBrand.copy(flash)
                                )
                        )

                    } else {
                        Box(
                            modifier = Modifier
                                .height(25.dp)
                                .width(20.dp)
                                .clickable {
                                    isCursorPositionEnd = true
                                    cursorPosition.value = text.value.length +1

                                    textFieldValueState.value = TextFieldValue(
                                        text = textFieldValueState.value.text,
                                        selection = TextRange(text.value.length +1)
                                    )
                                }
                        )
                    }
                }
            }
        }
    }
}

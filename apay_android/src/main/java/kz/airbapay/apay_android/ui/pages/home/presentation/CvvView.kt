package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.TextFieldValue
import kz.airbapay.apay_android.data.constant.RegexConst
import kz.airbapay.apay_android.data.constant.cvv
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditText

@Composable
internal fun CvvView(
    actionClickInfo: (() -> Unit),
    cvvText: MutableState<TextFieldValue>,
    cvvError: MutableState<String?>,
    cvvFocusRequester: FocusRequester,
    emailFocusRequester: FocusRequester?,
    modifier: Modifier
) {
    val focusManager = LocalFocusManager.current

    ViewEditText(
        text = cvvText,
        regex = Regex(RegexConst.NOT_DIGITS),
        mask = "AAA",
        errorTitle = cvvError,
        focusRequester = cvvFocusRequester,
        placeholder = cvv(),
        keyboardActions = KeyboardActions(
            onNext = {
                emailFocusRequester?.requestFocus()
            },
            onDone = {
                focusManager.clearFocus(true)
            }
        ),
        keyboardOptions = KeyboardOptions.Default.copy(
            capitalization = KeyboardCapitalization.None,
            autoCorrect = false,
            keyboardType = KeyboardType.NumberPassword,
            imeAction = if (emailFocusRequester == null) ImeAction.Done else ImeAction.Next
        ),
        modifierRoot = modifier,
        actionOnTextChanged = {},
        actionClickInfo = actionClickInfo,
        visualTransformation = PasswordVisualTransformation()
    )

}
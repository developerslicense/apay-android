package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.InitActionIcon

@Composable
internal fun ViewEditText(
    actionOnTextChanged: (String) -> Unit,
    actionClickInfo: (() -> Unit)? = null,
    text: MutableState<TextFieldValue>,
    errorTitle: MutableState<String?>,
    placeholder: String,
    mask: String? = null,
    regex: Regex? = null,
    focusRequester: FocusRequester,
    modifierRoot: Modifier = Modifier,
    modifierChild: Modifier = Modifier,
    paySystemIcon: MutableState<Int?> = mutableStateOf(null),
    keyboardActions: KeyboardActions,
    keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    ),
    visualTransformation: VisualTransformation? = null,
    isDateExpiredMask: Boolean = false
) {

    val hasFocus = remember {
        mutableStateOf(false)
    }

    Column(
        modifier = modifierRoot
    ) {

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 0.dp,
            border = BorderStroke(
                0.1.dp,
                if (errorTitle.value != null) ColorsSdk.stateError else ColorsSdk.gray20
            ),
            modifier = modifierChild
                .wrapContentHeight()
                .heightIn(min = 48.dp),
            onClick = {}
        ) {
            ConstraintLayout {
                val (clearIconRef, paySystemIconRef) = createRefs()

                CoreEditText(
                    isError = errorTitle.value != null,
                    mask = mask,
                    regex = regex,
                    placeholder = placeholder,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    hasFocus = hasFocus,
                    text = text,
                    focusRequester = focusRequester,
                    actionOnTextChanged = actionOnTextChanged,
                    visualTransformation = visualTransformation,
                    isDateExpiredMask = isDateExpiredMask,
                    actionClickInfo = actionClickInfo
                )

                InitIconPaySystem(
                    isError = errorTitle.value != null,
                    paySystemIconRef = paySystemIconRef,
                    clearIconRef = clearIconRef,
                    paySystemIcon = paySystemIcon.value,
                    text = text.value.text,
                    hasFocus = hasFocus.value
                )

                InitIconClear(
                    clearIconRef = clearIconRef,
                    hasFocus = hasFocus.value,
                    text = text.value.text,
                    isError = errorTitle.value != null,
                    actionClickClear = {
                        text.value = TextFieldValue(
                            text = "",
                            selection = TextRange(0)
                        )
                    }
                )
            }
        }

        if (errorTitle.value != null) {
            Row {
                Icon(
                    painter = painterResource(R.drawable.alarm),
                    contentDescription = "alarm",
                    tint = ColorsSdk.stateError,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
                Text(
                    style = LocalFonts.current.caption400,
                    text = errorTitle.value!!,
                    color = ColorsSdk.stateError,
                    modifier = Modifier.padding(vertical = 4.dp)
                )
            }

        }
    }
}


@Composable
private fun ConstraintLayoutScope.InitIconPaySystem(
    isError: Boolean,
    hasFocus: Boolean,
    text: String,
    paySystemIcon: Int?,
    paySystemIconRef: ConstrainedLayoutReference,
    clearIconRef: ConstrainedLayoutReference
) {
    if (
        text.isNotBlank()
        && paySystemIcon != null
    ) {
        InitActionIcon(
            action = null,
            iconSrc = paySystemIcon,
            modifier = Modifier
                .size(40.dp)
                .constrainAs(paySystemIconRef) {
                    if (hasFocus) {
                        end.linkTo(clearIconRef.start, margin = 2.dp)
                    } else {
                        end.linkTo(parent.end, margin = 4.dp)
                    }
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            _outlinedButtonColor = if (isError) ColorsSdk.stateBgError else ColorsSdk.bgBlock
        )
    }
}

@Composable
private fun ConstraintLayoutScope.InitIconClear(
    isError: Boolean,
    text: String,
    hasFocus: Boolean,
    actionClickClear: () -> Unit,
    clearIconRef: ConstrainedLayoutReference
) {
    if (
        text.isNotBlank()
        && hasFocus
    ) {
        InitActionIcon(
            action = actionClickClear,
            iconSrc = R.drawable.ic_close,
            modifier = Modifier
                .size(40.dp)
                .constrainAs(clearIconRef) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            _outlinedButtonColor = if (isError) ColorsSdk.stateBgError else ColorsSdk.bgBlock
        )
    }
}
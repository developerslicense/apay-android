package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.InitActionIcon
import kz.airbapay.apay_android.R

@Composable
internal fun ViewEditTextSimple(
    viewModel: EditTextViewModel,
    modifierRoot: Modifier = Modifier,
    modifierChild: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None
) {}

@Composable
internal fun ViewEditText(
    text: MutableState<String>,
    errorTitle: MutableState<String?>,
    placeholder: String,
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
    visualTransformation: VisualTransformation = VisualTransformation.None

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
            border = BorderStroke(0.1.dp, if (errorTitle.value != null) ColorsSdk.stateError else ColorsSdk.gray20),
            modifier = modifierChild
                .wrapContentHeight()
                .heightIn(min = 48.dp),
            onClick = {}
        ) {
            ConstraintLayout {
                val (clearIconRef, paySystemIconRef) = createRefs()

                CoreEditText(
                    placeholder = placeholder,
                    keyboardActions = keyboardActions,
                    keyboardOptions = keyboardOptions,
                    visualTransformation = visualTransformation,
                    hasFocus = hasFocus,
                    text = text,
                    focusRequester = focusRequester
                )

                InitIconPaySystem(
                    paySystemIconRef = paySystemIconRef,
                    clearIconRef = clearIconRef,
                    paySystemIcon = paySystemIcon.value,
                    text = text.value,
                )

                InitIconClear(
                    clearIconRef = clearIconRef,
                    hasFocus = hasFocus.value,
                    text = text.value,
                    actionClickClear = { }
                )
            }
        }

        if (errorTitle.value != null) {
            Text(
                style = LocalFonts.current.caption400,
                text = errorTitle.value!!,
                color = ColorsSdk.stateError,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}


@Composable
private fun ConstraintLayoutScope.InitIconPaySystem(
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
                    end.linkTo(clearIconRef.start, margin = 2.dp)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                }
        )
    }
}

@Composable
private fun ConstraintLayoutScope.InitIconClear(
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
                }
        )
    }
}
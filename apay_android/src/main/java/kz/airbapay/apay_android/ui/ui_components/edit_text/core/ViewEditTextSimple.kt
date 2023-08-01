package kz.airbapay.apay_android.ui.ui_components.edit_text.core

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
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
) {

    Column(
        modifier = modifierRoot
    ) {

        Card(
            shape = RoundedCornerShape(8.dp),
            elevation = 0.dp,
            border = BorderStroke(0.1.dp, viewModel.borderColor.value),
            backgroundColor = viewModel.backgroundColor.value,
            modifier = modifierChild
                .wrapContentHeight()
                .heightIn(min = 48.dp),
            enabled = viewModel.content.value.stateEnabled,
            onClick = {}
        ) {
            ConstraintLayout {
                val (iconEndRef) = createRefs()

                CoreEditText(
                    visualTransformation = visualTransformation,
                    viewModel = viewModel
                )

                InitIconEnd(
                    viewModel = viewModel,
                    iconEndRef = iconEndRef
                )
            }
        }

        if (viewModel.content.value.errorTitle != null
            && viewModel.content.value.stateError
        ) {
            Text(
                style = LocalFonts.current.caption400,
                text = viewModel.content.value.errorTitle.orEmpty(),
                color = ColorsSdk.stateError,
                modifier = Modifier.padding(vertical = 4.dp)
            )
        }
    }
}


@Composable
private fun ConstraintLayoutScope.InitIconEnd(
    viewModel: EditTextViewModel,
    iconEndRef: ConstrainedLayoutReference
) {
    if (
        (viewModel.content.value.endIcon != null
                || viewModel.text.value.isNotBlank())
        && viewModel.content.value.actionClickIcon != null
    ) {
        InitActionIcon(
            action = viewModel.content.value.actionClickIcon,
            iconSrc = viewModel.content.value.endIcon ?: R.drawable.ic_close,
            modifier = Modifier
                .size(40.dp)
                .constrainAs(iconEndRef) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                },
            _outlinedButtonColor = viewModel.content.value.editTextFieldBackgroundDefault
        )
    }
}
package kz.airbapay.apay_android.ui.ui_components

import android.annotation.SuppressLint
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstrainedLayoutReference
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.constraintlayout.compose.ConstraintLayoutScope
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
fun ViewButton(
    title: String,
    textColor: Color = ColorsSdk.textInversion,
    backgroundColor: Color = ColorsSdk.mainBrand,
    actionClick: (() -> Unit),
    modifierRoot: Modifier = Modifier,
    modifierChild: Modifier = Modifier.initConstraintModifier(),
    upperCase: Boolean = false,
    textStyle: TextStyle = LocalFonts.current.button
) {
    val kc = LocalSoftwareKeyboardController.current

    Card(
        shape = RoundedCornerShape(4.dp),
        elevation = 0.dp,
        backgroundColor = backgroundColor,
        modifier = modifierRoot
            .height(40.dp)
            .clickable(
                role = Role.Button,
                enabled = true,
                onClick = {
                    kc?.hide()
                    actionClick()
                }
            )

    ) {
        ConstraintLayout(
            modifier = modifierChild,
        ) {
            val (titleRef) = createRefs()

            val modifierTitle = this.initTitleModifier(title, titleRef)

            Text(
                modifier = modifierTitle,
                text = if (upperCase) title.uppercase() else title,
                color = textColor,
                style = textStyle
            )

        }
    }
}

private fun Modifier.initConstraintModifier() =
    this
        .fillMaxWidth()
        .padding(
            13.dp,
            10.dp,
            13.dp,
            10.dp
        )

@SuppressLint("ModifierFactoryExtensionFunction")
private fun ConstraintLayoutScope.initTitleModifier(
    title: String,
    titleRef: ConstrainedLayoutReference
) =
    Modifier.constrainAs(titleRef) {
        top.linkTo(parent.top, 0.dp)
        bottom.linkTo(parent.bottom, 0.dp)
        end.linkTo(parent.end, 9.dp)
        start.linkTo(parent.start, 9.dp)
    }.semantics { contentDescription = title }



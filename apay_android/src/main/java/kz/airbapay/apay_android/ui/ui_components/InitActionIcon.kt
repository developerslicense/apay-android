package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun InitActionIcon(
    modifier: Modifier = Modifier.size(50.dp),
    action: (() -> Unit)?,
    iconSrc: Int,
    _outlinedButtonColor: Color? = null

) {
    val outlinedButtonColor: Color = _outlinedButtonColor ?: ColorsSdk.bgBlock

    Box(
        modifier = modifier
            .background(color = outlinedButtonColor)
            .border(border = BorderStroke(0.dp, Color.Transparent))
            .clickable(
                enabled = (action != null),
                onClick = { action?.invoke() }
            ),
        contentAlignment = Alignment.Center
    ) {
        LoadImageSrc(iconSrc)
    }
}
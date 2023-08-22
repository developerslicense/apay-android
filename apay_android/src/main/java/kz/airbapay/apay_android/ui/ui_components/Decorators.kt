package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun LineDecorator(
    horizontalPadding: Int = 0
) {
    Spacer(
        modifier = Modifier
            .padding(horizontal = horizontalPadding.dp)
            .fillMaxWidth()
            .height(1.dp)
            .background(ColorsSdk.gray5)
    )
}

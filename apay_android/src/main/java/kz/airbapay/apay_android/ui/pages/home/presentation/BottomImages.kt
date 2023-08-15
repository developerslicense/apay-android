package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc

@Composable
internal fun BottomImages() {
    Spacer(modifier = Modifier.height(35.dp))

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center
    ) {

        LoadImageSrc(
            imageSrc = R.drawable.master_card,
            modifier = Modifier.height(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        LoadImageSrc(
            imageSrc = R.drawable.visa,
            modifier = Modifier.height(32.dp)
        )

        Spacer(modifier = Modifier.width(16.dp))
        LoadImageSrc(
            imageSrc = R.drawable.american_express,
            modifier = Modifier.height(32.dp)
        )
    }

    Spacer(modifier = Modifier.height(350.dp))
}
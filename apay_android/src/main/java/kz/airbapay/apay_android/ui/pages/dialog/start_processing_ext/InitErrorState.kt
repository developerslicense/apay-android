package kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.somethingWentWrong
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun InitErrorState() {
    Image(
        painter = painterResource(R.drawable.something_wrong),
        contentDescription = "something_wrong",
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    Text(
        text = somethingWentWrong(),
        style = LocalFonts.current.h3,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(20.dp))
}
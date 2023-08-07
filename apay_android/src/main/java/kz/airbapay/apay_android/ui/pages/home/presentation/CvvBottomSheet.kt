package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.cvvInfo
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.InitHeader

@Composable
internal fun CvvBottomSheet(
    actionClose: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            InitHeader(
                title = "CVV",
                actionClose = actionClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorsSdk.gray0)
            )

            Text(
                text = cvvInfo(),
                style = LocalFonts.current.regular,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
                    .padding(top = 22.dp)
            )
        }
    }
}


package kz.airbapay.apay_android.ui.pages.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.amountOfPurchase
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc

@Composable
internal fun DialogStartProcessing(
    actionClose: () -> Unit,
    purchaseAmount: String? = null
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
                title = paymentByCard(),
                actionClose = actionClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorsSdk.gray0)
            )

            InitAmount(purchaseAmount)

            Spacer(modifier = Modifier.height(16.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = ColorsSdk.bgGPAY,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                    .padding(16.dp)
            ) {
                LoadImageSrc(imageSrc = R.drawable.g_pay)
            }
        }
    }

}

@Composable
private fun InitAmount(
    purchaseAmount: String?
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(top = 24.dp)
            .fillMaxWidth()
            .background(
                color = ColorsSdk.bgMain,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomEnd = 8.dp,
                    bottomStart = 8.dp
                )
            )
            .padding(16.dp)
    ) {
        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier.fillMaxWidth()
        ) {
            Text(
                text = amountOfPurchase(),
                textAlign = TextAlign.Start,
                style = LocalFonts.current.regular
            )
            Text(
                text = purchaseAmount.orEmpty(),
                textAlign = TextAlign.End,
                style = LocalFonts.current.semiBold
            )
        }
    }
}
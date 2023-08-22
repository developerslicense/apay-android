package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.amountOfPurchase
import kz.airbapay.apay_android.data.constant.numberOfPurchase
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun TopInfoView(
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
        InitRow(
            textStart = amountOfPurchase(),
            textEnd = purchaseAmount.orEmpty()
        )
        Spacer(modifier = Modifier.height(8.dp))
        InitRow(
            textStart = numberOfPurchase(),
            textEnd = DataHolder.orderNumber
        )
    }
}

@Composable
private fun InitRow(
    textStart: String = "",
    textEnd: String = "",
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        modifier = Modifier.fillMaxWidth()
    ) {
        Text(
            text = textStart,
            textAlign = TextAlign.Start,
            style = LocalFonts.current.regular
        )
        Text(
            text = textEnd,
            textAlign = TextAlign.End,
            style = LocalFonts.current.semiBold
        )
    }
}
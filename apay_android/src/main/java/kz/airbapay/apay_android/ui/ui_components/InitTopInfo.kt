package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
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
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun TopInfoView(
    purchaseAmount: String?,
    numberOfPurchase: String?
) {

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .recomposeHighlighter()
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
            modifier = Modifier
                .recomposeHighlighter()
                .fillMaxWidth()
        ) {
            Text(
                text = amountOfPurchase(),
                textAlign = TextAlign.Start,
                style = LocalFonts.current.regular
            )
            Text(
                text = purchaseAmount.orEmpty(),
                textAlign = TextAlign.End,
                style = LocalFonts.current.semiBold,
                modifier = Modifier.recomposeHighlighter()
            )
        }

        Row(
            horizontalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .recomposeHighlighter()
                .fillMaxWidth()
                .padding(top = 8.dp)
        ) {
            Text(
                text = numberOfPurchase(),
                textAlign = TextAlign.Start,
                style = LocalFonts.current.regular
            )
            Text(
                text = numberOfPurchase ?: "",
                textAlign = TextAlign.End,
                style = LocalFonts.current.semiBold,
                modifier = Modifier.recomposeHighlighter()
            )
        }
    }
}


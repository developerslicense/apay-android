package kz.airbapay.apay_android.ui.pages.dialog.dialog_start_processing_ext

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.orPayWithCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.LineDecorator
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc
import kz.airbapay.apay_android.ui.ui_components.LoadImageUrl

@Composable
internal fun InitDialogStartProcessingCards(
    savedCards: List<BankCard>,
    selectedCard: MutableState<BankCard?>
) {
    val selected = remember {
        mutableStateOf(0)
    }

    Spacer(modifier = Modifier.height(32.dp))
    Text(
        style = LocalFonts.current.regular,
        text = orPayWithCard()
    )
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(
            count = savedCards.size,
            itemContent = { index ->
                val card = savedCards[index]
                InitCard(
                    card = card,
                    isSelected = index == selected.value,
                    isFirst = index == 0,
                    clickOnCard = {
                        selected.value = index
                        selectedCard.value = card
                    }
                )

            }
        )
    }

    Spacer(modifier = Modifier.height(32.dp))
    InitDialogStartProcessingPayWithNewCard(
        actionClick = {

        }
    )
}

@Composable
private fun InitCard(
    card: BankCard,
    isSelected: Boolean,
    isFirst: Boolean,
    clickOnCard: () -> Unit
) {
    if (!isFirst) {
        LineDecorator(16)
    }

    Column(
        modifier = Modifier
            .clickable { clickOnCard() }
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically
            ) {
                LoadImageUrl(
                    imageUrl = if(card.type.isNullOrBlank()) "https" else card.type,
                    errorImageSrc = null,
                    progressImageSrc = null
                )
//                LoadImageUrl(imageSrc = R.drawable.visa)  //todo

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    style = LocalFonts.current.semiBold,
                    text = card.maskedPan.orEmpty()
                )
            }

            LoadImageSrc(
                imageSrc = if (isSelected) R.drawable.ic_radio_button_on else R.drawable.ic_radio_button_off,
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}
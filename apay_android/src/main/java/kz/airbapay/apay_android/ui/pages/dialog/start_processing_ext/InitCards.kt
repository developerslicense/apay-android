package kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.AirbaPayActivity
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.LineDecorator
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc

@Composable
internal fun InitViewStartProcessingCards(
    savedCards: List<BankCard>,
    selectedCard: MutableState<BankCard?>,
    actionClose: () -> Unit,
    customSuccessPage: @Composable (() -> Unit)?
) {
    val context = LocalContext.current
    val selected = remember {
        mutableStateOf(0)
    }

    /*Spacer(modifier = Modifier.height(32.dp)) //todo временно
    Text(
        style = LocalFonts.current.regular,
        text = orPayWithCard()
    )*/
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
    InitViewStartProcessingPayWithNewCard(
        actionClick = {
            actionClose()
            AirbaPayActivity.init(
                context = context,
                customSuccessPage = customSuccessPage
            )
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
                LoadImageSrc(imageSrc = card.typeIcon)

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    style = LocalFonts.current.semiBold,
                    text = card.getMaskedPanCleared()
                )
            }

            if (isSelected) {
                Card(
                    shape = RoundedCornerShape(45.dp),
                    backgroundColor = ColorsSdk.colorBrandMainMS.value,
                    modifier = Modifier.size(20.dp),
                    elevation = 0.dp
                ) {
                    Card(
                        shape = RoundedCornerShape(45.dp),
                        backgroundColor = ColorsSdk.bgBlock,
                        modifier = Modifier.padding(6.dp)
                    ) {}
                }

            } else {
                Card(
                    shape = RoundedCornerShape(45.dp),
                    backgroundColor = ColorsSdk.gray15,
                    modifier = Modifier.size(20.dp),
                    elevation = 0.dp
                ) {
                    Card(
                        shape = RoundedCornerShape(45.dp),
                        backgroundColor = ColorsSdk.bgBlock,
                        modifier = Modifier.padding(2.dp)
                    ) {}
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

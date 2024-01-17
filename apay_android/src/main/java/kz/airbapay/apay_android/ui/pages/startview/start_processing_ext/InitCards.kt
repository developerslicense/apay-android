package kz.airbapay.apay_android.ui.pages.startview.start_processing_ext

import android.app.Activity
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
import androidx.compose.runtime.MutableIntState
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.orPayWithCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.openHome
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.LineDecorator
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc

@Composable
internal fun InitViewStartProcessingCards(
    savedCards: List<BankCard>,
    selectedCard: MutableState<BankCard?>,
    selectedIndex: MutableIntState,
) {
    val activity = LocalContext.current as Activity

    Spacer(modifier = Modifier.height(32.dp))
    Text(
        style = LocalFonts.current.regular,
        text = orPayWithCard()
    )
    Spacer(modifier = Modifier.height(16.dp))

    LazyColumn(
        modifier = Modifier
            .recomposeHighlighter()
            .fillMaxWidth()
            .wrapContentHeight()
    ) {
        items(
            count = savedCards.size,
            itemContent = { index ->
                val card = savedCards[index]
                InitCard(
                    card = card,
                    isSelected = index == selectedIndex.intValue,
                    isFirst = index == 0,
                    clickOnCard = {
                        selectedIndex.intValue = index
                        selectedCard.value = card
                    }
                )

            }
        )
    }

    Spacer(modifier = Modifier.height(32.dp))
    InitViewStartProcessingPayWithNewCard(
        actionClick = {
            openHome(activity)
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
            .recomposeHighlighter()
            .clickable { clickOnCard() }
    ) {

        Spacer(modifier = Modifier.height(16.dp))

        Row(
            modifier = Modifier
                .recomposeHighlighter()
                .padding(horizontal = 16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.recomposeHighlighter()
            ) {
                LoadImageSrc(imageSrc = card.typeIcon)

                Spacer(modifier = Modifier.width(16.dp))

                Text(
                    style = LocalFonts.current.semiBold,
                    text = card.getMaskedPanCleared(),
                    modifier = Modifier.recomposeHighlighter()
                )
            }

            if (isSelected) {
                Card(
                    shape = RoundedCornerShape(45.dp),
                    backgroundColor = ColorsSdk.colorBrand,
                    elevation = 0.dp,
                    modifier = Modifier
                        .recomposeHighlighter()
                        .size(20.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(45.dp),
                        backgroundColor = ColorsSdk.bgBlock,
                        modifier = Modifier
                            .recomposeHighlighter()
                            .padding(6.dp)
                    ) {}
                }

            } else {
                Card(
                    shape = RoundedCornerShape(45.dp),
                    backgroundColor = ColorsSdk.gray15,
                    elevation = 0.dp,
                    modifier = Modifier
                        .recomposeHighlighter()
                        .size(20.dp)
                ) {
                    Card(
                        shape = RoundedCornerShape(45.dp),
                        backgroundColor = ColorsSdk.bgBlock,
                        modifier = Modifier
                            .recomposeHighlighter()
                            .padding(2.dp)
                    ) {}
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

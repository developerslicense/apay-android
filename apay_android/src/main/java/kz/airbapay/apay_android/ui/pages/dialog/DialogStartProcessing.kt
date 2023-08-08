package kz.airbapay.apay_android.ui.pages.dialog

import android.content.Intent
import androidx.compose.foundation.background
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.AirbaPayActivity
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.amountOfPurchase
import kz.airbapay.apay_android.data.constant.orPayWithCard
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.constant.paymentByCard2
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.LineDecorator
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc
import kz.airbapay.apay_android.ui.ui_components.LoadImageUrl
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun DialogStartProcessing(// todo нужно добавить запрос на получение карт
    actionClose: () -> Unit,
    needShowGPay: Boolean = true,
    purchaseAmount: String? = null
) {
    BackHandler {
        actionClose()
    }

    val showProgressBar = remember {
        mutableStateOf(false)//true
    }

    val selectedCard = rememberSaveable {
        mutableStateOf<BankCard?>(null)
    }

    val a = listOf(
        BankCard(maskedPan = "**1234"),
        BankCard(maskedPan = "**4563"),
    )
    val savedCards = rememberSaveable {
        mutableStateOf<List<BankCard>>(a) //(emptyList())
    }


    Card(
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InitHeader(
                title = paymentByCard(),
                actionClose = actionClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorsSdk.gray0)
            )

            InitAmount(purchaseAmount)

            if (needShowGPay) {
                InitGPay()
            }

            if (savedCards.value.isNotEmpty()) {
                InitCards(
                    savedCards = savedCards.value,
                    selectedCard
                )
            }

            InitButtonNext(
                savedCards = savedCards.value,
                actionClose = actionClose
            )
        }
    }

    if (showProgressBar.value) {
        ProgressBarView()
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

@Composable
private fun InitGPay() {
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

@Composable
private fun InitCards(
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
                    imageUrl = card.type ?: "http",
                    errorImageSrc = null,
                    progressImageSrc = null
                )
//                LoadImageUrl(imageSrc = R.drawable.visa) //todo

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

@Composable
private fun InitButtonNext(
    savedCards: List<BankCard>,
    actionClose: () -> Unit,
) {
    val context = LocalContext.current

    val buttonModifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
        .padding(bottom = 32.dp)

    if (savedCards.isNotEmpty()) {
        ViewButton(
            title = "${payAmount()} ${DataHolder.purchaseAmount}",
            actionClick = {
                actionClose()
                val intent = Intent(context, AirbaPayActivity::class.java)
                intent.putExtra("", "")//todo
                context.startActivity(intent)
            },
            modifierRoot = buttonModifier
        )

    } else {
        ViewButton(
            title = paymentByCard2(),
            actionClick = {
                actionClose()
                val intent = Intent(context, AirbaPayActivity::class.java)
                context.startActivity(intent)
            },
            modifierRoot = buttonModifier
        )
    }
}

package kz.airbapay.apay_android.ui.pages.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.ui.pages.dialog.dialog_start_processing_ext.InitDialogStartProcessingAmount
import kz.airbapay.apay_android.ui.pages.dialog.dialog_start_processing_ext.InitDialogStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.dialog.dialog_start_processing_ext.InitDialogStartProcessingCards
import kz.airbapay.apay_android.ui.pages.dialog.dialog_start_processing_ext.InitDialogStartProcessingGPay
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

@Composable
internal fun DialogStartProcessing(//  todo нужно добавить запрос на получение карт
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
    val savedCards = remember {
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

            InitDialogStartProcessingAmount(purchaseAmount)

            if (needShowGPay) {
                InitDialogStartProcessingGPay()
            }

            if (savedCards.value.isNotEmpty()) {
                InitDialogStartProcessingCards(
                    savedCards = savedCards.value,
                    selectedCard
                )
            }

            InitDialogStartProcessingButtonNext(
                savedCards = savedCards.value,
                actionClose = actionClose
            )
        }
    }

    if (showProgressBar.value) {
        ProgressBarView()
    }
}

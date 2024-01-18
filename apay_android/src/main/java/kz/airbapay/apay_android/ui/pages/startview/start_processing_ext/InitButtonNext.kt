package kz.airbapay.apay_android.ui.pages.startview.start_processing_ext

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.constant.paymentByCard2
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.openHome
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.ui.pages.startview.bl.startSavedCard
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun InitViewStartProcessingButtonNext(
    savedCards: List<BankCard>,
    purchaseAmount: String?,
    isAuthenticated: Boolean,
    isLoading: MutableState<Boolean>,
    selectedCard: MutableState<BankCard?>,
    showCvv: () -> Unit
) {
    val activity = LocalContext.current as Activity

    val buttonModifier = Modifier
        .recomposeHighlighter()
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
        .padding(bottom = 32.dp)

    if (savedCards.isNotEmpty()
        && isAuthenticated
    ) {
        ViewButton(
            title = "${payAmount()} $purchaseAmount",
            actionClick = {
                isLoading.value = true
                startSavedCard(
                    activity = activity,
                    cardId = selectedCard.value?.id ?: "",
                    cvv = selectedCard.value?.cvv,
                    showCvv = showCvv
                )
            },
            modifierRoot = buttonModifier
        )

    } else {
        ViewButton(
            title = paymentByCard2(),
            actionClick = {
                openHome(activity)
            },
            modifierRoot = buttonModifier
        )
    }
}

package kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext

import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.AirbaPayActivity
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.constant.paymentByCard2
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun InitViewStartProcessingButtonNext(
    savedCards: List<BankCard>,
    actionClose: () -> Unit,
    purchaseAmount: String?,
    isAuthenticated: Boolean,
    selectedCard: MutableState<BankCard?>,
    customSuccessPage: @Composable (() -> Unit)?
) {
    val context = LocalContext.current

    val buttonModifier = Modifier
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
        .padding(bottom = 32.dp)

    if (savedCards.isNotEmpty()
        && isAuthenticated
    ) {
        ViewButton(
            title = "${payAmount()} $purchaseAmount",
            actionClick = {
                actionClose()
                AirbaPayActivity.init(
                    context = context,
                    cardId = selectedCard.value?.id,
                    customSuccessPage = customSuccessPage
                )
            },
            modifierRoot = buttonModifier
        )

    } else {
        ViewButton(
            title = paymentByCard2(),
            actionClick = {
                actionClose()
                AirbaPayActivity.init(
                    context = context,
                    customSuccessPage = customSuccessPage
                )
            },
            modifierRoot = buttonModifier
        )
    }
}

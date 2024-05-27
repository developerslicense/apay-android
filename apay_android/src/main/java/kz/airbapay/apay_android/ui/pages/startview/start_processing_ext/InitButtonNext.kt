package kz.airbapay.apay_android.ui.pages.startview.start_processing_ext

import android.app.Activity
import android.util.Log
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.ui.bl_components.saved_cards.blCheckSavedCardNeedCvv
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun InitViewStartProcessingButtonNext(
    purchaseAmount: String?,
    isLoading: MutableState<Boolean>,
    selectedCard: MutableState<BankCard?>,
    showCvv: () -> Unit,
    modifier: Modifier
) {
    val activity = LocalContext.current as Activity

    ViewButton(
        title = "${payAmount()} $purchaseAmount",
        actionClick = {
            if (selectedCard.value != null) {
                blCheckSavedCardNeedCvv(
                    activity = activity,
                    selectedCard = selectedCard.value!!,
                    showCvv = showCvv,
                    isLoading = { b ->
                        isLoading.value = b
                    },
                    onError = null
                )
            } else {
                Log.e("AirbaPay", "Ошибка. Нет выбранной карты")
            }
        },
        modifierRoot = modifier
    )
}

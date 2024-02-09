package kz.airbapay.apay_android.ui.pages.startview.start_processing_ext

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.startview.bl.checkNeedCvv
import kz.airbapay.apay_android.ui.ui_components.ViewButton
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
internal fun InitViewStartProcessingButtonNext(
    purchaseAmount: String?,
    isLoading: MutableState<Boolean>,
    selectedCard: MutableState<BankCard?>,
    showCvv: () -> Unit,
    modifier: Modifier
) {
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    ViewButton(
        title = "${payAmount()} $purchaseAmount",
        actionClick = {
            initAuth(
                activity = activity,
                coroutineScope = coroutineScope,
                onFailed = {},
                onSuccess = {
                    DataHolder.isGooglePayFlow = false
                    isLoading.value = true
                    checkNeedCvv(
                        activity = activity,
                        cardId = selectedCard.value?.id ?: "",
                        showCvv = showCvv,
                        isLoading = isLoading
                    )
                }
            )
        },
        modifierRoot = modifier
    )
}

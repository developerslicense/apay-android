package kz.airbapay.apay_android.ui.pages.startview.start_processing_ext

import android.app.Activity
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.ui.pages.startview.bl.checkNeedCvv
import kz.airbapay.apay_android.ui.ui_components.ViewButton
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
internal fun InitViewStartProcessingButtonNext(
    purchaseAmount: String?,
    isLoading: MutableState<Boolean>,
    selectedCard: MutableState<BankCard?>,
    showCvv: () -> Unit
) {
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    val buttonModifier = Modifier
        .recomposeHighlighter()
        .padding(horizontal = 16.dp)
        .padding(top = 16.dp)
        .padding(bottom = 32.dp)

    ViewButton(
        title = "${payAmount()} $purchaseAmount",
        actionClick = {
            initAuth(
                activity = activity,
                coroutineScope = coroutineScope,
                onFailed = {},
                onSuccess = {
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
        modifierRoot = buttonModifier
    )
}

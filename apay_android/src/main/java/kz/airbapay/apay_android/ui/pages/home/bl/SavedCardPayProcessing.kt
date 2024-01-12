package kz.airbapay.apay_android.ui.pages.home.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.PaymentsRepository

internal fun startPaymentProcessing(
    activity: Activity,
    isLoading : MutableState<Boolean>,
    cardId: String,
    paymentsRepository: PaymentsRepository,
    coroutineScope: CoroutineScope
) {
    isLoading.value = true

    coroutineScope.launch {

        startCreatePayment(
            cardId = cardId,
            isLoading = isLoading,
            paymentsRepository = paymentsRepository,
            on3DS = { redirectUrl ->
                openAcquiring(
                    redirectUrl = redirectUrl,
                    activity = activity
                )
            },
            onError = { errorCode ->
                openErrorPageWithCondition(
                    errorCode = errorCode.code,
                    activity = activity
                )
            },
            onSuccess = {
                openSuccess(activity)
            }
        )
    }
}

private fun startCreatePayment(
    cardId: String,
    isLoading : MutableState<Boolean>,
    on3DS: (redirectUrl: String?) -> Unit,
    onSuccess: () -> Unit,
    onError: (ErrorsCode) -> Unit,
    paymentsRepository: PaymentsRepository,
) {
    paymentsRepository.createPayment(
        cardId = cardId,
        result = {
            when (it.status) {
                "new" -> { isLoading.value = false }
                "success",
                "auth" -> { onSuccess() }
                "secure3D" -> { on3DS(it.redirectURL) }
            }
        },
        error = {
            onError(ErrorsCode.error_1)
        }
    )
}

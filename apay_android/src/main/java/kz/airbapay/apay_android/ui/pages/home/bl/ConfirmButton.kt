package kz.airbapay.apay_android.ui.pages.home.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.utils.getNumberCleared
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal fun startPaymentProcessing(
    activity: Activity,
    isLoading: MutableState<Boolean>,
    saveCard: Boolean = false,
    cardNumber: String,
    dateExpired: String? = null,
    cvv: String? = null,
    coroutineScope: CoroutineScope
) {
    isLoading.value = true

    coroutineScope.launch {
        Repository.paymentsRepository?.startPaymentDefault(
            cvv = cvv ?: "",
            expiry = dateExpired ?: "",
            pan = getNumberCleared(cardNumber),
            cardSave = saveCard,
            error = {

                if (it.errorBody()?.string()?.contains("invalid pan") == true) {
                    openErrorPageWithCondition(
                        errorCode = ErrorsCode.error_5002.code,
                        activity = activity
                    )

                } else {
                    openErrorPageWithCondition(
                        errorCode = ErrorsCode.error_1.code,
                        activity = activity
                    )
                }
            },
            result = { entryResponse ->
                if (entryResponse.errorCode != "0") {
                    val error = initErrorsCodeByCode(entryResponse.errorCode?.toInt() ?: 1)
                    openErrorPageWithCondition(
                        errorCode = error.code,
                        activity = activity
                    )

                } else if (entryResponse.isSecure3D == true) {
                    openAcquiring(
                        redirectUrl = entryResponse.secure3D?.action,
                        activity = activity
                    )

                } else {
                    openSuccess(activity)
                }
            }
        )
    }
}
package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal fun startSavedCard(
    activity: Activity,
    cardId: String,
    cvv: String?,
    showCvv: () -> Unit,
    isLoading: MutableState<Boolean>? = null
) {

    Repository.paymentsRepository?.startPaymentSavedCard(
        cardId = cardId,
        cvv = cvv,
        result = {
            when (it.status) {
                "new" -> {
                    isLoading?.value = false
                    showCvv()
                }

                "success",
                "auth" -> {
                    openSuccess(activity)
                }

                "secure3D" -> {
                    openAcquiring(
                        redirectUrl = it.secure3D?.action,
                        activity = activity
                    )
                }

                "error" -> {
                    isLoading?.value = false
                    openErrorPageWithCondition(
                        errorCode = ErrorsCode.error_5006.code,
                        activity = activity
                    )
                }
            }
        },
        error = {
            isLoading?.value = false
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_5006.code,
                activity = activity
            )
        }
    )
}

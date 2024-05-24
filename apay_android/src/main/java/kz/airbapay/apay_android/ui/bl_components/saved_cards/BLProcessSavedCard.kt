package kz.airbapay.apay_android.ui.bl_components.saved_cards

import android.app.Activity
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal fun blProcessSavedCard(
    cardId: String,
    cvv: String?,
    isLoading: (Boolean) -> Unit,
    activity: Activity
) {
    Repository.paymentsRepository?.startPaymentSavedCard(
        cardId = cardId,
        cvv = cvv,
        result = {
            when (it.status) {

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
                    isLoading(false)
                    openErrorPageWithCondition(
                        errorCode = ErrorsCode.error_5006.code,
                        activity = activity
                    )
                }
            }
        },
        error = {
            isLoading(false)
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_5006.code,
                activity = activity
            )
        }
    )
}
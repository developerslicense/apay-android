package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal fun startSavedCard(
    activity: Activity,
    cardId: String,
    cvv: String?,
    needCvv: MutableState<Boolean>,
    isError: MutableState<Boolean>
) {
    isError.value = false

    Repository.paymentsRepository?.startPaymentSavedCard(
        cardId = cardId,
        cvv = cvv,
        result = {
            when (it.status) {
                "new" -> {
                    needCvv.value = true
                }

                "success",
                "auth" -> {
                    openSuccess(activity)
                }

                "secure3D" -> {
                    openAcquiring(
                        redirectUrl = it.redirectURL,
                        activity = activity
                    )
                }
            }
        },
        error = {
            needCvv.value = true
            isError.value = true
        }
    )
}

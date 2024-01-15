package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openHome
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal fun startSavedCard(
    activity: Activity,
    card: BankCard?
) {
    Repository.paymentsRepository?.createPayment(
        cardId = card?.id,
        result = {
            when (it.status) {
                "new" -> {
                    openHome(
                        activity = activity,
                        cardId = it.id,
                        cardDateExpired = card?.expiry,
                        cardPan = card?.maskedPan
                    )
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
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        }
    )
}
package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.network.repository.startAuth

internal fun initPayments(
    activity: Activity,
    onGooglePayResult: (String?) -> Unit
) {
    Repository.paymentsRepository?.createPayment(
        error = {
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        },
        result = { response ->

            if (DataHolder.featureGooglePay) {
                authForGooglePay(
                    paymentCreateResponse = response,
                    activity = activity,
                    onGooglePayResult = onGooglePayResult
                )

            } else {
                onGooglePayResult(null)
            }
        }
    )
}

private fun authForGooglePay(
    paymentCreateResponse: PaymentCreateResponse,
    activity: Activity,
    onGooglePayResult: (String?) -> Unit,
) {
    startAuth(
        authRepository = Repository.authRepository!!,
        paymentId = paymentCreateResponse.id,
        onError = {
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        },
        onSuccess = {
            loadGooglePayButton(onGooglePayResult)
        }
    )
}

private fun loadGooglePayButton(
    onGooglePayResult: (String?) -> Unit
) {
    Repository.googlePayRepository?.getGooglePay(
        result = { response ->
            onGooglePayResult(response.buttonUrl)
        },
        error = {
            onGooglePayResult(null)
        }
    )
}

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
    onGooglePayResult: (String?) -> Unit,
    onError: () -> Unit = {
        openErrorPageWithCondition(
            errorCode = ErrorsCode.error_1.code,
            activity = activity
        )
    }
) {
    Repository.paymentsRepository?.createPayment(
        error = { onError() },
        result = { response ->
            authWithPaymentIdAndForGooglePay(
                paymentCreateResponse = response,
                onError = onError,
                onGooglePayResult = onGooglePayResult
            )
        }
    )
}

private fun authWithPaymentIdAndForGooglePay(
    paymentCreateResponse: PaymentCreateResponse,
    onGooglePayResult: (String?) -> Unit,
    onError: () -> Unit
) {
    startAuth(
        authRepository = Repository.authRepository!!,
        paymentId = paymentCreateResponse.id,
        onError = onError,
        onSuccess = {
            if (DataHolder.featureGooglePay) {
                loadGooglePayButton(onGooglePayResult)

            } else {
                onGooglePayResult(null)
            }
        }
    )
}

private fun loadGooglePayButton(
    onGooglePayResult: (String?) -> Unit
) {
    Repository.googlePayRepository?.getGooglePayButton(
        result = { response ->
            onGooglePayResult(response.buttonUrl)
        },
        error = {
            onGooglePayResult(null)
        }
    )
}

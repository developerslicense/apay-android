package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.network.repository.startAuth

internal fun initPayments(
    activity: Activity,
    isLoading : MutableState<Boolean>,
    onGooglePayLoadSuccess: (String?) -> Unit
) {
    Repository.paymentsRepository?.createPayment(
        error = {
            isLoading.value = false
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        },
        result = { response ->
            authForGooglePay(
                paymentCreateResponse = response,
                isLoading = isLoading,
                activity = activity,
                onGooglePayLoadSuccess = onGooglePayLoadSuccess
            )
        }
    )
}

private fun authForGooglePay(
    paymentCreateResponse: PaymentCreateResponse,
    isLoading: MutableState<Boolean>,
    activity: Activity,
    onGooglePayLoadSuccess: (String?) -> Unit
) {
    startAuth(
        authRepository = Repository.authRepository!!,
        paymentId = paymentCreateResponse.id,
        onError = {
            isLoading.value = false
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        },
        onResult = {
            loadGooglePayButton(
                onGooglePayLoadSuccess = onGooglePayLoadSuccess,
                isLoading = isLoading
            )
        }
    )
}

private fun loadGooglePayButton(
    onGooglePayLoadSuccess: (String?) -> Unit,
    isLoading: MutableState<Boolean>
) {
    Repository.googlePayRepository?.getGooglePay(
        result = { response ->
            onGooglePayLoadSuccess(response.buttonUrl)
            isLoading.value = false
        },
        error = {
            isLoading.value = false
        }
    )
}

package kz.airbapay.apay_android.ui.pages.googlepay

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.model.AuthRequest
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.encode
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.pages.startview.bl.initPayments
import kz.airbapay.apay_android.ui.ui_components.initAuth

class AirbaPayGooglePayViewModel: ViewModel() {

    fun auth(
        activity: Activity,
        onError:() -> Unit,
        onSuccess:() -> Unit
    ) {
        val authRequest = AuthRequest(
            paymentId = null,
            password = DataHolder.password,
            terminalId = DataHolder.terminalId,
            user = DataHolder.shopId
        )
        Repository.initRepositories(activity.applicationContext)

        Repository.authRepository?.auth(
            param = authRequest,
            result = { response ->
                DataHolder.accessToken = response.accessToken

                Repository.googlePayRepository?.getGooglePayMerchant(
                    result = {
                        DataHolder.gatewayMerchantId = it.gatewayMerchantId
                        DataHolder.gateway = it.gateway

                        initPayments(
                            activity = activity,
                            onGooglePayResult = { onSuccess() },
                            onError = onError
                        )
                    },
                    error = { onError() }
                )
            },
            error = { onError() }
        )
    }

    fun authBiometry(
        activity: Activity,
        onSuccess: () -> Unit
    ) {
        initAuth(
            activity = activity,
            coroutineScope = viewModelScope,
            onSuccess = { onSuccess() },
            onFailed = {},
            onNotSecurity = { onSuccess() }
        )
    }

    fun processingWallet(
        googlePayToken: String,
        activity: Activity
    ) {

        Repository.paymentsRepository?.startPaymentWallet(
            googlePayToken = googlePayToken.encode(),
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
            },
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
            }
        )
    }
}
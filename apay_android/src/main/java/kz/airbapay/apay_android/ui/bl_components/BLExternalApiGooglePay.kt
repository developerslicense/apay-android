package kz.airbapay.apay_android.ui.bl_components

import android.app.Activity
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.model.GooglePayMerchantResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.encode
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal fun blGetGooglePayMerchantIdAndGateway(
    onSuccess: (GooglePayMerchantResponse) -> Unit,
    onError: () -> Unit
) {
    Repository.googlePayRepository?.getGooglePayMerchant(
        result = {
            DataHolder.gatewayMerchantId = it.gatewayMerchantId
            DataHolder.gateway = it.gateway
            onSuccess(it)
        },
        error = { onError() }
    )
}

internal fun blProcessGooglePay(
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

            if (it?.errorBody()?.string()?.contains("invalid pan") == true) {
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

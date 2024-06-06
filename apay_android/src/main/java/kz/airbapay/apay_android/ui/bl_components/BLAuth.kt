package kz.airbapay.apay_android.ui.bl_components

import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.model.AuthRequest
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.Repository

internal fun blAuth(
    onSuccess: (String) -> Unit,
    onError: () -> Unit,
    paymentId: String? = null,
    password: String? = null,
    terminalId: String? = null,
    shopId: String? = null,
    repository: AuthRepository = Repository.authRepository!!
) {

    val authRequest = AuthRequest(
        paymentId = paymentId,
        password = password,
        terminalId = terminalId,
        user = shopId
    )

    repository.auth(
        param = authRequest,
        result = {
            onSuccess(it.accessToken ?: "")
        },
        error = {
            onError()
        }
    )
}

internal fun blUpdateToken(
    paymentId: String,
    onSuccess: (AirbaPaySdk.CreatePaymentResult) -> Unit,
    onError: () -> Unit
) {
    Repository.authRepository?.updateAuth(
        paymentId = paymentId,
        result = {
            val r = AirbaPaySdk.CreatePaymentResult(
                token = DataHolder.token ?: "",
                paymentId = paymentId
            )
            onSuccess(r)
        },
        error = {
            onError()
        }
    )
}
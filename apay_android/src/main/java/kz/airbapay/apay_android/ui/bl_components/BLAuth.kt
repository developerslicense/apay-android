package kz.airbapay.apay_android.ui.bl_components

import kz.airbapay.apay_android.data.model.AuthRequest
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
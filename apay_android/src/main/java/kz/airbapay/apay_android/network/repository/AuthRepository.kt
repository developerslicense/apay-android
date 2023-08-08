package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.AuthRequest
import kz.airbapay.apay_android.data.model.AuthResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class AuthRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun auth(
        param: AuthRequest,
        result: (AuthResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.auth(param)
                }
            },
            result = { body ->
                body.body()?.let {
                    DataHolder.accessToken = it.accessToken
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }
}

internal fun startAuth(
    authRepository: AuthRepository,
    onResult: () -> Unit,
    onError: () -> Unit,
    paymentId: String? = null
) {
    val authRequest = AuthRequest(
        paymentId = paymentId,
        password = DataHolder.password,
        terminalId = DataHolder.terminalId,
        user = DataHolder.shopId
    )

    authRepository.auth(
        param = authRequest,
        result = {
            onResult()
        },
        error = {
            onError()
        }
    )
}
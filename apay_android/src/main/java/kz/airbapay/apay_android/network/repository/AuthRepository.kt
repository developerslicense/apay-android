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
        error: (Response<*>?) -> Unit
    ) {
        launch(
            paramsForLog = param,
            requestFlow = {
                safeApiFlowCall {
                    api.auth(param)
                }
            },
            result = { body ->
                body.body()?.let {
                    DataHolder.token = it.accessToken
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }

    fun updateAuth(
        paymentId: String,
        result: (AuthResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {
        launch(
            paramsForLog = paymentId,
            requestFlow = {
                safeApiFlowCall {
                    api.updateAuth(paymentId)
                }
            },
            result = { body ->
                body.body()?.let {
                    DataHolder.token = it.accessToken
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }
}

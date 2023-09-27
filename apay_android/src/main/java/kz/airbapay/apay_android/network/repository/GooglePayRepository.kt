package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.GooglePayButtonResponse
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class GooglePayRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun getGooglePay(
        result: (GooglePayButtonResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getGooglePay()
                }
            },
            result = { body ->
                body.body()?.let {
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }
}
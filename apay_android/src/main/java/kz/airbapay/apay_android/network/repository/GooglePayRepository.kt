package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.GooglePayButtonResponse
import kz.airbapay.apay_android.data.model.GooglePayMerchantResponse
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class GooglePayRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun getGooglePayButton(
        result: (GooglePayButtonResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getGooglePayButton()
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

    fun getGooglePayMerchant(
        result: (GooglePayMerchantResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getGooglePayMerchant()
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
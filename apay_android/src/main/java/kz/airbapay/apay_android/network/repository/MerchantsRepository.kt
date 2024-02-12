package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.MerchantsResponse
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class MerchantsRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun getMerchantInfo(
        result: (MerchantsResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getMerchantInfo()
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
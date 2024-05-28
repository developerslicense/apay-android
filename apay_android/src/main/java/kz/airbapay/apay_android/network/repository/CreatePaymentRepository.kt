package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class CreatePaymentRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun createPayment(
        goods: List<AirbaPaySdk.Goods>?,
        settlementPayments: List<AirbaPaySdk.SettlementPayment>?,
        result: (PaymentCreateResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {

        DataHolder.isGooglePayFlow = false

        val param = initParamsForCreatePaymentV1(
            goods = goods,
            settlementPayments = settlementPayments
        )

        launch(
            paramsForLog = param,
            requestFlow = {
                safeApiFlowCall { api.createPayment(param) }
            },
            result = { body ->
                body.body()?.let {
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }

    private fun initParamsForCreatePaymentV1(
        goods: List<AirbaPaySdk.Goods>?,
        settlementPayments: List<AirbaPaySdk.SettlementPayment>?
    ): HashMap<String, Any?> {
        val cart = HashMap<String, Any?>().apply {
            put("goods", goods)
        }

        val param = HashMap<String, Any?>().apply {
            put("account_id", DataHolder.accountId)
            put("amount", DataHolder.purchaseAmount)
            put("cart", cart)
            put("currency", "KZT")
            put("description", "description")
            if (!DataHolder.userEmail.isNullOrEmpty()) {
                put("email", DataHolder.userEmail)
            }
            put("invoice_id", DataHolder.invoiceId)
            put("language", DataHolder.currentLang)
            put("order_number", DataHolder.orderNumber)
            put("phone", DataHolder.userPhone)
            put("auto_charge", DataHolder.autoCharge)
            put("failure_back_url", DataHolder.failureBackUrl)
            put("failure_callback", DataHolder.failureCallback)
            put("success_back_url", DataHolder.successBackUrl)
            put("success_callback", DataHolder.successCallback)

            /** параметр, нужный, если несколько айдишников компаний*/
            if (!settlementPayments.isNullOrEmpty()) {
                val settlement = HashMap<String, Any>()
                settlement["payments"] = settlementPayments
                put("settlement", settlement)
            }
        }
        return param
    }
}


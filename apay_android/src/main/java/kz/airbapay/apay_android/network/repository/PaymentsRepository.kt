package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.model.PaymentEntryRequest
import kz.airbapay.apay_android.data.model.PaymentEntryResponse
import kz.airbapay.apay_android.data.model.PaymentInfoResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class PaymentsRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun createPayment(
        saveCard: Boolean,
        result: (PaymentCreateResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {

        val param = initParamsForCreatePayment(saveCard)

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.createPayment(param)
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

    fun createPayment(
        cardId: String,
        result: (PaymentCreateResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {

        val param = initParamsForCreatePayment(
            saveCard = null,
            cardId = cardId
        )

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.createPayment(
                        param = param,
                        cardId = cardId
                    )
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

    private fun initParamsForCreatePayment(
        saveCard: Boolean? = null,
        cardId: String? = null
    ): HashMap<String, Any?> {
        val cart = HashMap<String, Any?>().apply {
            put("goods", DataHolder.goods)
        }

        val param = HashMap<String, Any?>().apply {
            put("account_id", DataHolder.userPhone)
            put("amount", DataHolder.purchaseAmount.toDouble())
            saveCard?.let { put("card_save", saveCard) }
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
            cardId?.let { put("card_id", cardId) }
            put("auto_charge", 0)
            put("failure_back_url", DataHolder.failureBackUrl)
            put("failure_callback", DataHolder.failureCallback)
            put("success_back_url", DataHolder.successBackUrl)
            put("success_callback", DataHolder.successCallback)

            /** параметр, нужный, если несколько айдишников компаний*/
            if (!DataHolder.settlementPayments.isNullOrEmpty()) {
                val settlement = HashMap<String, Any>()
                settlement["payments"] = DataHolder.settlementPayments!!
                put("settlement", settlement)
            }
        }
        return param
    }

    fun getPaymentInfo(
        result: (PaymentInfoResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getPaymentInfo()
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

    fun paymentAccountEntry(
        param: PaymentEntryRequest,
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.paymentAccountEntry(param)
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

    fun paymentAccountEntryRetry(
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.paymentAccountEntryRetry()
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

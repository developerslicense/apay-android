package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.model.PaymentEntryResponse
import kz.airbapay.apay_android.data.model.PaymentInfoResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.getNumberCleared
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

        val cart = HashMap<String, Any?>().apply {
            put("goods", DataHolder.goods)
        }

        val param = HashMap<String, Any?>().apply {
            put("account_id", DataHolder.userPhone)
            put("amount", getNumberCleared(DataHolder.purchaseAmount).toDouble())
            put("card_save", saveCard)
            put("cart", cart)
            put("currency", "KZT")
            put("description", "description")
            put("email", DataHolder.userEmail)
            put("invoice_id", DataHolder.invoiceId)
            put("language", DataHolder.currentLang)
            put("order_number", DataHolder.orderNumber)
            put("phone", DataHolder.userPhone)
            put("card_id", DataHolder.cardId)
            put("auto_charge", 0)
            put("failure_back_url", DataHolder.failureBackUrl)
            put("failure_callback", DataHolder.failureCallback)
            put("success_back_url", DataHolder.successBackUrl)
            put("success_callback", DataHolder.successCallback)

            /** не обязательный параметр, нужно присылать, если есть необходимость в разделении счетов по компаниям*/
            if (!DataHolder.settlementPayments.isNullOrEmpty()) {
                val settlement = HashMap<String, Any>().apply {
                    put("payments", DataHolder.settlementPayments)
                }
                put("settlement", settlement)
            }
        }

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

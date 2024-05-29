package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.GetCvvResponse
import kz.airbapay.apay_android.data.model.GooglePaymentWallet
import kz.airbapay.apay_android.data.model.GooglePaymentWalletRequest
import kz.airbapay.apay_android.data.model.PaymentEntryResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class PaymentsRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun startPaymentDefault(
        cvv: String,
        expiry: String,
        pan: String,
        cardSave: Boolean,
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {

        val param = HashMap<String, Any?>().apply {
            val card = HashMap<String, Any?>().apply {
                put("cvv", cvv)
                put("expiry", expiry)
                put("pan", pan)
                put("card_name", "Card Holder")
            }

            addScreenParams()

            put("card", card)
            put("card_save", cardSave)
            put("email", DataHolder.userEmail)
            put("send_receipt", true)

        }

        launch(
            paramsForLog = param,
            requestFlow = {
                safeApiFlowCall { api.putPayment(param) }
            },
            result = { body ->
                body.body()?.let {
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }

    fun startPaymentSavedCard(
        cardId: String,
        cvv: String?,
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {

        val param = HashMap<String, Any?>().apply {
            put("cvv", cvv ?: "")
            addScreenParams()
        }

        launch(
            paramsForLog = param,
            requestFlow = {
                safeApiFlowCall {
                    api.putPayment(
                        cardId = cardId,
                        param = param
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

    fun startPaymentWallet(
        googlePayToken: String,
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {
        DataHolder.isGooglePayFlow = true

        val wallet = GooglePaymentWallet(token = googlePayToken)
        val param = GooglePaymentWalletRequest(wallet = wallet)

        launch(
            paramsForLog = param,
            requestFlow = {
                safeApiFlowCall {
                    api.putPaymentWallet(param)
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

    private fun HashMap<String, Any?>.addScreenParams() {
        if (DataHolder.height != null && DataHolder.width != null) {
            val params = HashMap<String, Any?>().apply {
                put("screen_height", DataHolder.height)
                put("screen_width", DataHolder.width)
            }

            put("params", params)
        }
    }

    fun paymentAccountEntryRetry(
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>?) -> Unit
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

    fun paymentGetCvv(
        cardId: String,
        result: (GetCvvResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getCvv(cardId)
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
        result: (PaymentEntryResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {

        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getPayment()
                }
            },
            result = { body ->
                body.body()?.let {
                    DataHolder.purchaseAmount = it.amount ?: 0.0
                    DataHolder.purchaseAmountFormatted.value = Money.initDouble(DataHolder.purchaseAmount).getFormatted()
                    DataHolder.orderNumber = it.orderNumber ?: ""
                    DataHolder.invoiceId = it.invoiceId ?: ""
                    DataHolder.accountId = it.accountId ?: ""
                    DataHolder.renderSecurityCvv = it.addParameters?.payForm?.requestCvv
                    DataHolder.renderSecurityBiometry = it.addParameters?.payForm?.requestFaceId
                    DataHolder.renderSavedCards = it.addParameters?.payForm?.renderSaveCards
                    DataHolder.renderGooglePay = it.addParameters?.payForm?.renderGooglePay

                    DataHolder.failureBackUrl = it.failureBackUrl ?: DataHolder.failureBackUrl
                    DataHolder.successBackUrl = it.successBackUrl ?: DataHolder.successBackUrl

                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }
}

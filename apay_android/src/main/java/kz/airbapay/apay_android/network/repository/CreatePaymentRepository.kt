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
        failureCallback: String,
        successCallback: String,
        autoCharge: Int = 0,
        purchaseAmount: Double,
        invoiceId: String,
        orderNumber: String,
        renderSecurityCvv: Boolean?,
        renderSecurityBiometry: Boolean?,
        renderGooglePay: Boolean?,
        renderSavedCards: Boolean?,
        goods: List<AirbaPaySdk.Goods>?,
        settlementPayments: List<AirbaPaySdk.SettlementPayment>?,
        result: (PaymentCreateResponse) -> Unit,
        error: (Response<*>?) -> Unit
    ) {

        DataHolder.isGooglePayFlow = false

        val param = initParamsForCreatePaymentV1(
            goods = goods,
            settlementPayments = settlementPayments,
            failureCallback = failureCallback,
            successCallback = successCallback,
            autoCharge = autoCharge,
            purchaseAmount = purchaseAmount,
            orderNumber = orderNumber,
            invoiceId = invoiceId,
            renderSecurityCvv = renderSecurityCvv,
            renderSecurityBiometry = renderSecurityBiometry,
            renderSavedCards = renderSavedCards,
            renderGooglePay = renderGooglePay
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
        failureCallback: String,
        successCallback: String,
        autoCharge: Int = 0,
        purchaseAmount: Double,
        invoiceId: String,
        orderNumber: String,
        renderSecurityCvv: Boolean?,
        renderSecurityBiometry: Boolean?,
        renderGooglePay: Boolean?,
        renderSavedCards: Boolean?,
        goods: List<AirbaPaySdk.Goods>?,
        settlementPayments: List<AirbaPaySdk.SettlementPayment>?
    ): HashMap<String, Any?> {
        val cart = HashMap<String, Any?>().apply {
            put("goods", goods)
        }

        val payform = HashMap<String, Any?>().apply {
            val payformInner = HashMap<String, Any?>().apply {
                if (renderGooglePay != null) {
                    put("render_google_pay", renderGooglePay)
                }
                if (renderSavedCards != null) {
                    put("render_save_cards", renderSavedCards)
                }
                if (renderSecurityCvv != null) {
                    put("request_cvv", renderSecurityCvv)
                }
                if (renderSecurityBiometry != null) {
                    put("request_face_id", renderSecurityBiometry)
                }
            }
            put("payform", payformInner)
        }

        val param = HashMap<String, Any?>().apply {
            put("account_id", DataHolder.accountId)
            put("amount", purchaseAmount)
            put("cart", cart)
            put("currency", "KZT")
            put("description", "description")
            if (!DataHolder.userEmail.isNullOrEmpty()) {
                put("email", DataHolder.userEmail)
            }
            put("language", DataHolder.currentLang)
            put("phone", DataHolder.userPhone)
            put("invoice_id", invoiceId)
            put("order_number", orderNumber)
            put("auto_charge", autoCharge)
            put("failure_back_url", DataHolder.failureBackUrl)
            put("failure_callback", failureCallback)
            put("success_back_url", DataHolder.successBackUrl)
            put("success_callback", successCallback)

            put("add_parameters", payform)

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


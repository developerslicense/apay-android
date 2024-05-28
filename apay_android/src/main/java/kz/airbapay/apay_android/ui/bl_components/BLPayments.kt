package kz.airbapay.apay_android.ui.bl_components

import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.CreatePaymentRepository
import kz.airbapay.apay_android.network.repository.Repository

internal fun blCreatePayment(
    repository: CreatePaymentRepository = Repository.createPaymentsRepository!!,
    authToken: String,
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
    settlementPayments: List<AirbaPaySdk.SettlementPayment>? = null,
    onSuccess: (String) -> Unit,
    onError: () -> Unit
) {

    DataHolder.token = authToken

    repository.createPayment(
        result = { response ->
            blUpdateToken(response.id!!, onSuccess, onError)
        },
        error = { onError() },
        goods = goods,
        settlementPayments = settlementPayments,
        failureCallback = failureCallback,
        successCallback = successCallback,
        autoCharge = autoCharge,
        purchaseAmount = purchaseAmount,
        orderNumber = orderNumber,
        invoiceId = invoiceId,
        renderGooglePay = renderGooglePay,
        renderSavedCards = renderSavedCards,
        renderSecurityBiometry = renderSecurityBiometry,
        renderSecurityCvv = renderSecurityCvv
    )
}


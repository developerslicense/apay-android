package kz.airbapay.apay_android.ui.bl_components

import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.CreatePaymentRepository
import kz.airbapay.apay_android.network.repository.Repository

internal fun blCreatePayment(
    repository: CreatePaymentRepository = Repository.createPaymentsRepository!!,
    authToken: String,
    goods: List<AirbaPaySdk.Goods>?,
    settlementPayments: List<AirbaPaySdk.SettlementPayment>? = null,
    onSuccess: (String) -> Unit,
    onError: () -> Unit
) {

    DataHolder.token = authToken

    repository.createPayment(
        result = { response ->
            Repository.authRepository?.updateAuth(
                paymentId = response.id!!,
                result = {
                    onSuccess(response.id)
                },
                error = {
                    onError()
                }
            )
        },
        error = { onError() },
        goods = goods,
        settlementPayments = settlementPayments
    )
}


package kz.airbapay.apay_android.ui.bl_components

import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.CreatePaymentRepository
import kz.airbapay.apay_android.network.repository.Repository

internal fun blCreatePaymentV1(
    repository: CreatePaymentRepository = Repository.createPaymentsRepository!!,
    authToken: String,
    goods: List<AirbaPaySdk.Goods>,
    settlementPayments: List<AirbaPaySdk.SettlementPayment>? = null,
    onSuccess: (String) -> Unit,
    onError: () -> Unit
) {

    DataHolder.token = authToken

    repository.createPaymentV1(
        result = { response ->
            onSuccess(response.id!!)
        },
        error = { onError() },
        goods = goods,
        settlementPayments = settlementPayments
    )
}

/*
internal fun blCreatePaymentV2(
    repository: CreatePaymentRepository = Repository.createPaymentsRepository!!,
    authToken: String,
    onSuccess: (String) -> Unit,
    onError: () -> Unit
) {

    DataHolder.token = authToken

    repository.createPaymentV2(
        result = { response ->
            onSuccess(response.id!!)
        },
        error = { onError() }
    )
}*/

package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class PaymentCreateResponse(
    @SerializedName("invoice_id")
    val invoiceId: String?,

    @SerializedName("id")
    val id: String?
)
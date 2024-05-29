package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class PaymentEntryRequest(
    @SerializedName("card_save")
    val cardSave: Boolean?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("send_receipt")
    val sendReceipt: Boolean? = null,

    @SerializedName("card")
    val card: BankCard?
)

internal data class PaymentEntryResponse(
    @SerializedName("secure3D")
    val secure3D: Secure3D?,

    @SerializedName("error_code")
    val errorCode: String?,

    @SerializedName("account_id")
    val accountId: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("error_message")
    val errorMessage: String?,

    @SerializedName("is_retry")
    val isRetry: Boolean?, // если true, то можно через кнопку "повторить"

    @SerializedName("is_secure3D")
    val isSecure3D: Boolean?,

    @SerializedName("payform_url")
    val payFormUrl: String?,

    @SerializedName("add_parameters")
    val addParameters: AddParameters?,

    @SerializedName("amount")
    val amount: Double?,

    @SerializedName("invoice_id")
    val invoiceId: String?,

    @SerializedName("order_number")
    val orderNumber: String?,

    @SerializedName("failure_back_url")
    val failureBackUrl: String?,

    @SerializedName("success_back_url")
    val successBackUrl: String?,

)

package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal class PaymentInfoResponse(
    @SerializedName("secure3D")
    val secure3D: Secure3D?,

    @SerializedName("card")
    val card: BankCard?,

    @SerializedName("card_save")
    val cardSave: Boolean?,

    @SerializedName("is_retry")
    val isRetry: Boolean?,

    @SerializedName("amount")
    val amount: Long?,

    @SerializedName("error_code")
    val errorCode: Int?,

    @SerializedName("bank_code")
    val bankCode: String?,

    @SerializedName("card_id")
    val cardId: String?,

    @SerializedName("action")
    val action: String?,

    @SerializedName("created")
    val created: String?,

    @SerializedName("currency")
    val currency: String?,

    @SerializedName("description")
    val description: String?,

    @SerializedName("email")
    val email: String?,

    @SerializedName("error_message")
    val errorMessage: String?,

    @SerializedName("expired")
    val expired: String?,

    @SerializedName("success_back_url")
    val successBackUrl: String?,

    @SerializedName("failure_back_url")
    val failureBackUrl: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("invoice_id")
    val invoiceId: String?,

    @SerializedName("language")
    val language: String?,

    @SerializedName("order_number")
    val orderNumber: String?,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("terminal_id")
    val terminalId: String?,

    @SerializedName("type")
    val type: String?,

)

internal class Secure3D(
    @SerializedName("action")
    val action: String?,

    @SerializedName("md")
    val md: String?,

    @SerializedName("pa_req")
    val paReq: String?,

    @SerializedName("term_url")
    val termUrl: String?,

)
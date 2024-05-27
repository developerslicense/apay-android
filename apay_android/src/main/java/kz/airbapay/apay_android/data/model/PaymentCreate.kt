package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class PaymentCreateResponse(
    @SerializedName("invoice_id")
    val invoiceId: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("status")
    val status: String?,

    @SerializedName("redirectURL")
    val redirectURL: String?,

    @SerializedName("add_parameters")
    val addParameters: AddParameters?
)

internal data class AddParameters(

    @field:SerializedName("payform")
    val payForm: PayForm? = null
)

internal data class PayForm(

    @field:SerializedName("render_google_pay")
    val renderGooglePay: Boolean? = null,

    @field:SerializedName("request_cvv")
    val requestCvv: Boolean? = null,

    @field:SerializedName("request_face_id")
    val requestFaceId: Boolean? = null,

    @field:SerializedName("render_apple_pay")
    val renderApplePay: Boolean? = null,

    @field:SerializedName("render_save_cards")
    val renderSaveCards: Boolean? = null
)
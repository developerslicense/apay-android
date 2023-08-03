package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal class PaymentEntryRequest(
    @SerializedName("card_save")
    val cardSave: Boolean,

    @SerializedName("email")
    val email: String?,

    @SerializedName("send_receipt")
    val sendReceipt: Boolean,

    @SerializedName("card")
    val card: PaymentEntryCard
)

internal data class PaymentEntryCard(
    @SerializedName("card_name")
    val cardName: String?,

    @SerializedName("cvv")
    val cvv: String?,

    @SerializedName("expiry")
    val expiry: String?,

    @SerializedName("id")
    val cardId: String?,

    @SerializedName("pan")
    val pan: String?,
)

internal data class PaymentEntryResponse(
    @SerializedName("secure3D")
    val secure3D: Secure3D?,

    @SerializedName("error_code")
    val errorCode: String?,

    @SerializedName("error_message")
    val errorMessage: String?,

    @SerializedName("is_retry")
    val isRetry: Boolean?, // если true, то можно через кнопку "повторить"

    @SerializedName("is_secure3D")
    val isSecure3D: Boolean?,
)

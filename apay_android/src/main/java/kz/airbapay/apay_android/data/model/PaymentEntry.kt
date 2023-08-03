package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

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

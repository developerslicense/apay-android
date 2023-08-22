package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class AuthRequest(
    @SerializedName("password")
    val password: String?,

    @SerializedName("payment_id")
    val paymentId: String?,

    @SerializedName("terminal_id")
    val terminalId: String?,

    @SerializedName("user")
    val user: String?,
)

internal data class AuthResponse(
    @SerializedName("access_token")
    val accessToken: String?
)
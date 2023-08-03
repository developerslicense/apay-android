package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class CardAddRequest(
    @SerializedName("language")
    val language: String?,

    @SerializedName("account_id")
    val accountId: String?,

    @SerializedName("email")
    val email: String,

    @SerializedName("phone")
    val phone: String?,

    @SerializedName("failure_back_url")
    val failureBackUrl: String?,

    @SerializedName("failure_callback")
    val failureCallback: String,

    @SerializedName("success_back_url")
    val successBackUrl: String?,

    @SerializedName("success_callback")
    val successCallback: String?,

)

internal data class CardAddResponse(
    @SerializedName("id")
    val cardId: String?
)


package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class GetCvvResponse(
    @SerializedName("request_cvv")
    val requestCvv: Boolean
)
package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class Secure3D(
    @SerializedName("action")
    val action: String?,

    @SerializedName("md")
    val md: String?,

    @SerializedName("pa_req")
    val paReq: String?,

    @SerializedName("term_url")
    val termUrl: String?,

)
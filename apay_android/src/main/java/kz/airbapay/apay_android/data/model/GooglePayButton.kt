package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class GooglePayButtonResponse(
    @SerializedName("button_url")
    val buttonUrl: String?
)
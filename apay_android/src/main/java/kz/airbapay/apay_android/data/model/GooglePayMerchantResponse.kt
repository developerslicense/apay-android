package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

data class GooglePayMerchantResponse(

	@field:SerializedName("gateway_merchant_id")
	val gatewayMerchantId: String? = null,

	@field:SerializedName("merchant_name")
	val merchantName: String? = null,

	@field:SerializedName("merchant_id")
	val merchantId: String? = null,

	@field:SerializedName("merchant_origin")
	val merchantOrigin: String? = null,

	@field:SerializedName("gateway")
	val gateway: String? = null
)

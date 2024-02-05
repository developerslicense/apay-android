package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class MerchantsResponse(

	@field:SerializedName("configuration")
	val configuration: Configuration? = null,

	@field:SerializedName("logo_url")
	val logoUrl: String? = null,

	@field:SerializedName("name")
	val name: Name? = null,

	@field:SerializedName("id")
	val id: String? = null
)

internal data class Name(

	@field:SerializedName("ru")
	val ru: String? = null,

	@field:SerializedName("en")
	val en: String? = null,

	@field:SerializedName("kz")
	val kz: String? = null
)

internal data class Configuration(

	@field:SerializedName("render_google_pay_button")
	val renderGooglePayButton: Boolean? = null,

	@field:SerializedName("send_receipt")
	val sendReceipt: Boolean? = null,

	@field:SerializedName("save_card")
	val saveCard: Boolean? = null,

	@field:SerializedName("color")
	val color: String? = null,

	@field:SerializedName("render_header")
	val renderHeader: Boolean? = null,

	@field:SerializedName("render_save_cards")
	val renderSaveCards: Boolean? = null,

	@field:SerializedName("render_apple_pay_button")
	val renderApplePayButton: Boolean? = null
)

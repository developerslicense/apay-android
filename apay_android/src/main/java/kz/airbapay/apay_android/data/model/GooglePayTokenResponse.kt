package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal data class GooglePayTokenResponse(

	@field:SerializedName("apiVersionMinor")
	val apiVersionMinor: Int? = null,

	@field:SerializedName("apiVersion")
	val apiVersion: Int? = null,

	@field:SerializedName("paymentMethodData")
	val paymentMethodData: PaymentMethodData? = null,

	@field:SerializedName("shippingAddress")
	val shippingAddress: ShippingAddress? = null
)

internal data class PaymentMethodData(

	@field:SerializedName("description")
	val description: String? = null,

	@field:SerializedName("tokenizationData")
	val tokenizationData: TokenizationData? = null,

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("info")
	val info: GooglePayInfo? = null
)

internal data class AssuranceDetails(

	@field:SerializedName("cardHolderAuthenticated")
	val cardHolderAuthenticated: Boolean? = null,

	@field:SerializedName("accountVerified")
	val accountVerified: Boolean? = null
)

internal data class GooglePayInfo(

	@field:SerializedName("cardNetwork")
	val cardNetwork: String? = null,

	@field:SerializedName("cardDetails")
	val cardDetails: String? = null,

	@field:SerializedName("billingAddress")
	val billingAddress: BillingAddress? = null,

	@field:SerializedName("assuranceDetails")
	val assuranceDetails: AssuranceDetails? = null
)

internal data class ShippingAddress(

	@field:SerializedName("address3")
	val address3: String? = null,

	@field:SerializedName("sortingCode")
	val sortingCode: String? = null,

	@field:SerializedName("address2")
	val address2: String? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("postalCode")
	val postalCode: String? = null,

	@field:SerializedName("locality")
	val locality: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("administrativeArea")
	val administrativeArea: String? = null
)

internal data class TokenizationData(

	@field:SerializedName("type")
	val type: String? = null,

	@field:SerializedName("token")
	val token: String? = null
)

internal data class BillingAddress(

	@field:SerializedName("address3")
	val address3: String? = null,

	@field:SerializedName("sortingCode")
	val sortingCode: String? = null,

	@field:SerializedName("address2")
	val address2: String? = null,

	@field:SerializedName("address1")
	val address1: String? = null,

	@field:SerializedName("countryCode")
	val countryCode: String? = null,

	@field:SerializedName("postalCode")
	val postalCode: String? = null,

	@field:SerializedName("locality")
	val locality: String? = null,

	@field:SerializedName("name")
	val name: String? = null,

	@field:SerializedName("administrativeArea")
	val administrativeArea: String? = null
)

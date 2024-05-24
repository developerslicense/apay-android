package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.utils.DataHolder

internal data class GooglePaymentWalletRequest(

	@field:SerializedName("send_receipt")
	val sendReceipt: Boolean? = DataHolder.userEmail != null,

	@field:SerializedName("wallet")
	val wallet: GooglePaymentWallet? = null,

	@field:SerializedName("email")
	val email: String? = DataHolder.userEmail,

	@field:SerializedName("params")
	val params: GooglePaymentParams? = GooglePaymentParams()
)

internal data class GooglePaymentWallet(

	@field:SerializedName("type")
	val type: String = "GooglePay",

	@field:SerializedName("token")
	val token: String? = null
)

internal data class GooglePaymentParams(

	@field:SerializedName("screen_height")
	val screenHeight: Int? = DataHolder.height,

	@field:SerializedName("screen_width")
	val screenWidth: Int? = DataHolder.width
)

package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.utils.DataHolder

internal data class GooglePaymentWalletRequest(

	@field:SerializedName("send_receipt")
	val sendReceipt: Boolean? = DataHolder.userEmail != null,

	@field:SerializedName("wallet")
	val wallet: GooglePaymentWallet? = null,

	@field:SerializedName("email")
	val email: String? = DataHolder.userEmail
)

internal data class GooglePaymentWallet(

	@field:SerializedName("type")
	val type: String = "GooglePay",

	@field:SerializedName("token")
	val token: String? = null
)

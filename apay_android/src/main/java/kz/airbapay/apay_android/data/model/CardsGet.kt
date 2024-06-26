package kz.airbapay.apay_android.data.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

internal class CardsGetResponse : ArrayList<BankCard>()

@Parcelize
data class BankCard(
    @SerializedName("pan")
    val pan: String? = null,

    @SerializedName("account_id")
    val accountId: String? = null,

    @SerializedName("masked_pan")
    val maskedPan: String? = null,

    @SerializedName("expiry")
    val expiry: String? = null,

    @SerializedName("expire")
    val expiredForResponse: String? = null,

    @SerializedName("name")
    val name: String? = null,

    @SerializedName("id")
    val id: String? = null,

    @SerializedName("type")
    val type: String? = null,

    @SerializedName("issuer")
    val issuer: String? = null,

    @SerializedName("cvv")
    val cvv: String? = null,

    val typeIcon: Int? = null

) : Parcelable {
    fun getMaskedPanCleared() = maskedPan?.takeLast(6).orEmpty()
    fun getMaskedPanClearedWithPoint() = maskedPan?.takeLast(6)?.replace("*", "•").orEmpty()
 }

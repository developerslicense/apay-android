package kz.airbapay.apay_android.data.model

import com.google.gson.annotations.SerializedName

internal class CardsGetResponse : ArrayList<BankCard>()

internal data class BankCard(
    @SerializedName("pan")
    val pan: String?,

    @SerializedName("account_id")
    val accountId: String?,

    @SerializedName("masked_pan")
    val maskedPan: String?,

    @SerializedName("expire")
    val expire: String?,

    @SerializedName("name")
    val name: String?,

    @SerializedName("id")
    val id: String?,

    @SerializedName("type")
    val type: String?,

    @SerializedName("issuer")
    val issuer: String?,

)

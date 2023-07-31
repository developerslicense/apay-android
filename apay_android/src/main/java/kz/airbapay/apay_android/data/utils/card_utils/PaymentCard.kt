package kz.airbapay.apay_android.data.utils.card_utils

internal class PaymentCard(
    val type: CardType,
    val number: String,
    val name: String,
    val month: Int,
    val cvv: Int
)

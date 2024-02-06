package kz.airbapay.apay_android.data.utils.card_utils

import kz.airbapay.apay_android.data.utils.getNumberCleared

// https://en.wikipedia.org/wiki/Payment_card_number

internal fun getCardTypeFromNumber(
    input: String
) = if (input.contains(Regex("^((34)|(37))"))) {
    CardType.AMERICAN_EXPRESS

} else if (input.contains(Regex("^(62)"))) {
    CardType.CHINA_UNION_PAY

} else if (input.contains(Regex("^(220[0–4])"))) {
    CardType.MIR

} else if (input.contains(Regex("^((5018)|(5020)|(5038)|(5893)|(6304)|(6759)|(6761)|(6762)|(6763))"))) {
    CardType.MAESTRO

} else if (input.contains(Regex("^((5[1-5])|(222[1-9]|22[3-9][0-9]|2[3-6][0-9]{2}|27[01][0-9]|2720))"))) {
    CardType.MASTER_CARD

} else if (input.contains(Regex("^4"))) {
    CardType.VISA

} else {
    CardType.INVALID
}

internal fun validateCardNumWithLuhnAlgorithm(
    number: String?
) : Boolean {
    val input = getNumberCleared(
        amount = number,
    )
    if ((input.length) < 15) {
        return false
    }

    var sum = 0
    val length = input.length

    try {
        for (i in 0 until length) {
            // get digits in reverse order

            var digit = (input[length - i - 1]).toString().toInt()

            // every 2nd number multiply with 2
            if (i % 2 == 1) {
                digit *= 2
            }

            sum += if (digit > 9) (digit - 9) else digit
        }

        return (sum % 10 == 0)

    } catch (e: Exception) {
//        e.printStackTrace()
        return false
    }
}
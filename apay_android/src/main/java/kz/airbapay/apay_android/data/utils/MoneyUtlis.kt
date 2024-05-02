package kz.airbapay.apay_android.data.utils

import kz.airbapay.apay_android.data.constant.RegexConst.NOT_DIGITS_NOT_COMMA_NOT_NON_BREAK_SPACE
import kz.airbapay.apay_android.data.constant.kzt
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

// тесты в MoneyTests
internal class Money(
    val amount: Double = 0.0,
    val currency: String = kzt
) {

    companion object {
        fun initLong(
            amount: Long,
            currency: String = kzt
        ) = Money(
            amount = getNumberClearedWithMaxSymbol(
                amount = amount.toString(),
            ).toDouble(),
            currency = currency
        )

        fun initDouble(
            amount: Double,
            currency: String = kzt
        ) = Money(
            amount = amount,
            currency = currency
        )

        fun initString(
            amount: String,
            currency: String = kzt
        ) = Money(
            amount = getNumberClearedWithMaxSymbol(
                amount = amount,
            ).toDouble(),
            currency = currency
        )

        fun initMoney(
            amount: Money,
        ) = Money(
            amount = getNumberClearedWithMaxSymbol(
                amount = amount.amount.toString(),
            ).toDouble(),
            currency = amount.currency
        )

    }

    fun getFormatted(): String {
        return getMoneyFormatted(amount)
    }
}

internal fun getMoneyFormatted(amount: String, currency: String = "KZT"): String {
    return try {

        val format = NumberFormat.getInstance(Locale("ru"))
        format.currency = Currency.getInstance(currency)

        format.minimumFractionDigits = 0
        format.maximumFractionDigits = 2

        var tempAmount = getNumberClearedWithMaxSymbol(amount)

        while (tempAmount.startsWith("0")) {
            tempAmount = tempAmount.drop(1)
        }

        val amountNumberFormatted = format.format(tempAmount.toDouble())
        replaceCurrencyIso4217(amountNumberFormatted, currency)
    } catch (e: Exception) {
        e.printStackTrace()
        "0 $kzt"
    }
}

internal fun getMoneyFormatted(amount: Double, currency: String = "KZT"): String {
    return try {

        val format = NumberFormat.getInstance(Locale("ru"))
        format.currency = Currency.getInstance(currency)

        format.minimumFractionDigits = 0
        format.maximumFractionDigits = 2

        val amountNumberFormatted = format.format(amount)
        replaceCurrencyIso4217(amountNumberFormatted, currency)
    } catch (e: Exception) {
        e.printStackTrace()
        "0 $kzt"
    }
}

private fun replaceCurrencyIso4217(amount: String, currency: String?): String {
    return currency?.let {
        amount
            .replace("$currency ", "")
            .plus(" $kzt")
    } ?: amount.replace(Regex(NOT_DIGITS_NOT_COMMA_NOT_NON_BREAK_SPACE), "")
}
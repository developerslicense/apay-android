package kz.airbapay.apay_android.data.utils

import kz.airbapay.apay_android.data.constant.RegexConst.NOT_DIGITS
import kz.airbapay.apay_android.data.constant.RegexConst.NOT_DIGITS_NOT_COMMA_NOT_NON_BREAK_SPACE
import kz.airbapay.apay_android.data.constant.kzt
import java.math.BigDecimal
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

internal class Money(
    val amount: Int = 0,
    val currency: String = kzt
) {

    companion object {
        fun initInt(
            amount: Int,
            currency: String = kzt
        ) = Money(
            amount = getNumberClearedWithMaxSymbol(
                amount = amount.toString(),
                lang = DataHolder.currentLang
            ).toInt(),
            currency = currency
        )

        fun initDouble(
            amount: Double,
            currency: String = kzt
        ) = Money(
            amount = getNumberClearedWithMaxSymbol(
                amount = amount.toString(),
                lang = DataHolder.currentLang
            ).toInt(),
            currency = currency
        )

        fun initString(
            amount: String,
            currency: String = kzt
        ) = Money(
            amount = getNumberClearedWithMaxSymbol(
                amount = amount,
                lang = DataHolder.currentLang
            ).toInt(),
            currency = currency
        )

        fun initMoney(
            amount: Money,
        ) = Money(
            amount = amount.amount,
            currency = amount.currency
        )

    }

    fun getFormatted(): String {
        return getMoneyFormatted(amount.toString());
    }
}

private fun getMoneyFormatted(amount: String, currency: String? = null): String {
    return try {
        val format = NumberFormat.getInstance(Locale("KK-kk"))
        format.currency = Currency.getInstance(currency ?: kzt)

        format.minimumFractionDigits = 0
        format.maximumFractionDigits = 0 // отключил десятичные. было значение 2
        val amountNumberFormatted = format.format(BigDecimal(amount))
        replaceCurrencyIso4217(amountNumberFormatted, currency)
    } catch (e: Exception) {
        e.printStackTrace()

        try {
            amount.replace(Regex(NOT_DIGITS), "")
        } catch (e: Exception) {
            e.printStackTrace()
            "0"
        }
    }
}

private fun replaceCurrencyIso4217(amount: String, currency: String?): String {
    return currency?.let {
        amount
            .replace("$currency ", "")
            .plus(" $kzt")
    } ?: amount.replace(Regex(NOT_DIGITS_NOT_COMMA_NOT_NON_BREAK_SPACE), "")
}
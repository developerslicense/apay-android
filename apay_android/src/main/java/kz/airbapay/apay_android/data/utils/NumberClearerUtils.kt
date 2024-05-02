package kz.airbapay.apay_android.data.utils

import kz.airbapay.apay_android.data.constant.RegexConst

// тесты в GetNumberClearedTests
internal fun getNumberCleared(
    amount: String?,
    isUserEntered: Boolean = false,
    isPhoneNumber: Boolean = false
): String {
    val amountLocaleCleaned = clearNumberForLocale(amount, isUserEntered, isPhoneNumber)
    return clearNumberMaxSymbols(amountLocaleCleaned)
}

// тесты в GetNumberClearedWithMaxSizeTests
internal fun getNumberClearedWithMaxSymbol(
    amount: String?,
    isUserEntered: Boolean = false,
    isPhoneNumber: Boolean = false,
    maxSize: Int = 15
): String {
    val amountLocaleCleaned = clearNumberForLocale(amount, isUserEntered, isPhoneNumber)
    return clearNumberMaxSymbols(
        amountLocaleCleaned = amountLocaleCleaned,
        maxSize = maxSize,
        needClearMax = true
    )
}

private fun clearNumberForLocale(
    amount: String?,
    isUserEntered: Boolean,
    isPhoneNumber: Boolean
): String {
    var amount = amount ?: ""

    val regex = Regex(RegexConst.numberCleanRu)

    if (isPhoneNumber) {
        if (amount.startsWith("7 ")) {
            amount = amount.replace("+7", "").replaceFirst("7 ", "")

        } else if (amount.startsWith("8") || amount.contains("+7")) {
            amount = amount.replace("+7", "").replaceFirst("8", "")
        }
    }

    var amountLocaleCleaned = amount.replace(regex, "").replace(",", ".")

    val comas = amountLocaleCleaned.split(".")

    if (comas.size > 1) {
        try {
            amountLocaleCleaned = "${comas[0]}.${comas[1].ifEmpty { comas[2] }}"
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    if (amountLocaleCleaned.endsWith(".") || amountLocaleCleaned.endsWith(",")) {
        amountLocaleCleaned = amountLocaleCleaned.dropLast(1)
    }

    if (!isUserEntered && amount.startsWith("-")) {
        amountLocaleCleaned = "-$amountLocaleCleaned"
    }

    return amountLocaleCleaned
}

private fun clearNumberMaxSymbols(
    amountLocaleCleaned: String,
    maxSize: Int = 10,
    needClearMax: Boolean = false
): String {

    return if (needClearMax && amountLocaleCleaned.length > maxSize) {
        amountLocaleCleaned.substring(0, maxSize)

    } else {
        amountLocaleCleaned
    }
}

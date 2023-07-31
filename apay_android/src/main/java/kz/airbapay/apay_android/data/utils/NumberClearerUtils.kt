package kz.airbapay.apay_android.data.utils

import kz.airbapay.apay_android.data.constant.RegexConst

internal fun getNumberCleared(
    amount: String?,
    lang: String,
    isUserEntered: Boolean = false,
    isPhoneNumber: Boolean = false 
) : String {
  val amountLocaleCleaned = clearNumberForLocale(amount, lang, isUserEntered, isPhoneNumber)
  return clearNumberMaxSymbols(amountLocaleCleaned, isUserEntered)
}

internal fun getNumberClearedWithMaxSymbol(
    amount: String?,
    lang: String,
    isUserEntered: Boolean = false,
    isPhoneNumber: Boolean = false,
    maxSize: Int = 10
) : String {
  val amountLocaleCleaned = clearNumberForLocale(amount, lang, isUserEntered, isPhoneNumber)
  return clearNumberMaxSymbols(amountLocaleCleaned, isUserEntered, maxSize = maxSize, needClearMax = true)
}

private fun clearNumberForLocale(
    amount: String?,
    lang: String,
    isUserEntered: Boolean,
    isPhoneNumber: Boolean
) : String {
  var amount = amount ?: ""
  
  val regex = if (lang == "en") {
    Regex(RegexConst.numberCleanEn)
  } else {
    Regex(RegexConst.numberCleanRu)
  }

  if (isPhoneNumber) {
    if (amount.startsWith("7 ")) {
      amount = amount.replace("+7", "").replaceFirst("7 ", "")

    } else if (amount.startsWith("8") || amount.contains("+7")
    ) {
      amount = amount.replace("+7", "").replaceFirst("8", "")
    }
  }

  var amountLocaleCleaned = amount.replace(regex, "").replace(",", ".") ?: "0"

  val comas = amountLocaleCleaned.split(".")

  if (comas.size > 2) {
    try {
      amountLocaleCleaned = "${comas[0]}.${comas[1].ifEmpty { comas[2] }}"
    } catch (e: Exception) {
      messageLog("clearNumberForLocale error: $e")
    }
  }

  if (!isUserEntered && amount.startsWith("-")) {
    amountLocaleCleaned = "-$amountLocaleCleaned"
  }

  if (isUserEntered) {
    amountLocaleCleaned = amountLocaleCleaned.replace(".", "")
  }

  return amountLocaleCleaned
}

internal fun clearNumberMaxSymbols(
    amountLocaleCleaned: String,
    isUserEntered: Boolean,
    maxSize: Int = 10,
    needClearMax: Boolean = false
) : String {
  val amountSplited = amountLocaleCleaned.split(".")

    return if (needClearMax && amountSplited[0].length > maxSize) {
        amountSplited[0].substring(0, maxSize)

    } else if (isUserEntered && amountSplited.size > 1) {
        "${amountSplited[0]}.${amountSplited[1]}"

    } else if (amountSplited.size > 1) {
        clearDecimalMaxSymbols(amountSplited)

    } else {
        amountSplited[0]
    }
}

internal fun clearDecimalMaxSymbols(
    amountSplited: List<String>
) : String {
  var decimal = amountSplited[1]
  if (decimal.length > 2) {
    decimal = amountSplited[1].substring(0, 2)
  }

  return "${amountSplited[0]}.$decimal"
}

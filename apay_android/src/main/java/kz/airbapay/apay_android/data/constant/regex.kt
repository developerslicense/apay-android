package kz.airbapay.apay_android.data.constant

internal object RegexConst {
  const val NOT_DIGITS_NOT_COMMA_NOT_NON_BREAK_SPACE = "[^, 0-9]+"
  const val NOT_DIGITS = "[\\D]"
  const val DIGITS = "\\b([0-9]+)"
  const val CARD = "\\b([0-9] )"
  const val TEXTS = "[a-zA-Z -]"
  val numberCleanEn = "[^0-9.]"
  val numberCleanRu = "[^0-9,.]"
  val emailValidation = "^[a-zA-Z0-9.!#$%&'*+/=?^_`{|}~-]+@[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?(?:\\.[a-zA-Z0-9](?:[a-zA-Z0-9-]{0,253}[a-zA-Z0-9])?)*$"
}

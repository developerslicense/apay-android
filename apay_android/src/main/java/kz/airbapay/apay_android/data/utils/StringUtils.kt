package kz.airbapay.apay_android.data.utils

internal fun getValueFromArguments(
    resultArgument: List<String>,
    key: String
) : String? {
  return resultArgument
      .firstOrNull { element -> element.contains(key) }
      ?.split("=")
      ?.get(1)
}
package kz.airbapay.apay_android.data.utils

fun getValueFromArguments(
    resultArgument: List<String>,
    key: String
) : String? {
  return resultArgument
      .firstOrNull { element -> element.contains(key) }
      ?.split("=")
      ?.get(1)
}
package kz.airbapay.apay_android.data.utils.card_utils

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

// тесты в CardDateTest
internal fun isDateValid(
  value: String?
): Boolean {
  try {
    if (value?.isEmpty() == true
      || value == null
      || value.startsWith("00")
    ) {
      return false
    }

    val year: Int
    val month: Int
    // The value contains a forward slash if the month and year has been
    // entered.
    if (value.contains(Regex("(/)"))) {

      val split = value.split(Regex("(/)"))
      // The value before the slash is the month while the value to right of
      // it is the year.
      month = try {
        split[0].toInt()
      } catch (e: Exception) {
        e.printStackTrace()
        -1
      }

      year = try {
        split[1].toInt()
      } catch (e: Exception) {
        e.printStackTrace()
        0
      }

    } else { // Only the month was entered

      month = value.substring(0, (value.length)).toInt()
      year = -1 // Lets use an invalid year intentionally
    }

    if ((month < 1) || (month > 12)) {
      // A valid month is between 1 (January) and 12 (December)
      return false
    }

    val date = Date()
    val fourDigitsYear = if (year < 2000) "20$year".toInt() else year
    val nowYear = getYear(date).toInt()
    val nowMonth = getMonth(date).toInt()

    if ((fourDigitsYear < 2010) || (fourDigitsYear > 2099)) {

      // We are assuming a valid year should be between 1 and 2099.
      // Note that, it"s valid doesn"t mean that it has not expired.
      return false
    }

    return if (fourDigitsYear > nowYear) {
      true

    } else fourDigitsYear == nowYear
      && month > nowMonth

  } catch (e: Exception) {
    e.printStackTrace()

    return false
  }
}


private fun getYear(
  date: Date
): String {
    val dateFormat = SimpleDateFormat("yyyy", Locale("ru"))
    return dateFormat.format(date)

}

private fun getMonth(
  date: Date
): String {
    val dateFormat = SimpleDateFormat("MM", Locale("ru"))
    return dateFormat.format(date)
}
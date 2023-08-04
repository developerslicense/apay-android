package kz.airbapay.apay_android.test_for_card

import kz.airbapay.apay_android.data.utils.card_utils.isDateValid
import kz.airbapay.apay_android.isAssert
import org.junit.Test

internal class CardDateTest {

    @Test
    fun dateUtilsValidationTest() {
        isAssert(checkDateValid(null), false)
        isAssert(checkDateValid(""), false)
        isAssert(checkDateValid("0"), false)
        isAssert(checkDateValid("00"), false)
        isAssert(checkDateValid("000"), false)
        isAssert(checkDateValid("0000"), false)
        isAssert(checkDateValid("1"), false)
        isAssert(checkDateValid("13"), false)
        isAssert(checkDateValid("123"), false)
        isAssert(checkDateValid("1023"), false)
        isAssert(checkDateValid("1024"), false)
        isAssert(checkDateValid("1124"), false)
        isAssert(checkDateValid("1224"), false)
        isAssert(checkDateValid("0524"), false)
        isAssert(checkDateValid("0124"), false)
        isAssert(checkDateValid("0024"), false)

        for(i in 0..22) {
            checkError("0", i)
            checkError("1", i)
            checkError("2", i)
            checkError("3", i)
            checkError("4", i)
            checkError("5", i)
            checkError("6", i)
            checkError("7", i)
            checkError("8", i)
            checkError("9", i)
            checkError("00", i)
            checkError("13", i)
            checkError("14", i)
            checkError("20", i)
            checkError("50", i)
            checkError("01", i)
            checkError("02", i)
            checkError("03", i)
            checkError("04", i)
            checkError("05", i)
            checkError("06", i)
            checkError("07", i)
            checkError("08", i)
            checkError("09", i)
            checkError("10", i)
            checkError("11", i)
            checkError("12", i)
        }

        for(i in 24..99) {
            checkError("00", i)
            checkError("13", i)
            checkError("14", i)
            checkError("20", i)
            checkError("50", i)
            checkSuccess("01", i)
            checkSuccess("02", i)
            checkSuccess("03", i)
            checkSuccess("04", i)
            checkSuccess("05", i)
            checkSuccess("06", i)
            checkSuccess("07", i)
            checkSuccess("08", i)
            checkSuccess("09", i)
            checkSuccess("10", i)
            checkSuccess("11", i)
            checkSuccess("12", i)
        }

       /** здесь может вылетать ошибка в зависимости от месяца нынешнего года и самого года.
         поэтому, нужно менять год и checkError с checkSuccess в зависимости от того, какой сейчас
         месяц или год. на момент написания теста это было 4е августа 2023
         поэтому, были
         val year = 2023
         checkError("08", year)
         и checkSuccess("09", year)*/

        val year = 2023
        checkError("0", year)
        checkError("00", year)
        checkError("01", year)
        checkError("02", year)
        checkError("03", year)
        checkError("04", year)
        checkError("05", year)
        checkError("06", year)
        checkError("07", year)
        checkError("08", year)
        checkSuccess("09", year)
        checkSuccess("10", year)
        checkSuccess("11", year)
        checkSuccess("12", year)
        checkError("13", year)
        checkError("15", year)
        checkError("20", year)

    }

    private fun checkSuccess(
        month: String,
        year: Int
    ) {
        isAssert(checkDateValid("$month/$year"), true)
    }

    private fun checkError(
        month: String,
        year: Int
    ) {
        isAssert(checkDateValid("$month/$year"), false)
    }

    private fun checkDateValid(
        date: String?
    ) = isDateValid(date)
}
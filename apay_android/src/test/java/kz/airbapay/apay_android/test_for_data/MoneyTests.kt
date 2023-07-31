package kz.airbapay.apay_android.test_for_data

import kz.airbapay.apay_android.data.constant.kzt
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.data.utils.getMoneyFormatted
import kz.airbapay.apay_android.isAssert
import org.junit.Test

class MoneyTests {
    @Test
    fun testInitMoney() {
        isAssert(initMoneyAmountMoney(initMoney(0)), 0)
        isAssert(initMoneyAmountMoney(initMoney(123)), 123)
        isAssert(initMoneyAmountMoney(initMoney("012")), 12)
        isAssert(initMoneyAmountMoney(initMoney("012")).toString(), "12")
        isAssert(initMoneyAmountMoney(initMoney("0012")), 12)
        isAssert(initMoneyAmountMoney(initMoney("000012")), 12)
        isAssert(initMoneyAmountMoney(initMoney("000012")).toString(), "12")
        isAssert(initMoneyAmountMoney(initMoney("0000120")), 120)
        isAssert(initMoneyAmountMoney(initMoney("00001020")), 1020)
        isAssert(initMoneyAmountMoney(initMoney(5123)), 5123)
        isAssert(initMoneyAmountMoney(initMoney(123456)), 123456)
        isAssert(initMoneyAmountMoney(initMoney(1234567890)), 1234567890)
        isAssert(initMoneyAmountMoney(initMoney("12345678901234")), 1234567890)

        isAssert(initMoneyAmountLong(0), 0)
        isAssert(initMoneyAmountLong(123), 123)
//        isAssert(initMoneyAmountLong("012"), 12)
//        isAssert(initMoneyAmountLong("00012"), 12)
//        isAssert(initMoneyAmountLong("0000012"), 12)
        isAssert(initMoneyAmountLong(5123), 5123)
        isAssert(initMoneyAmountLong(123456), 123456)
        isAssert(initMoneyAmountLong(1234567890), 1234567890)
        isAssert(initMoneyAmountLong(12345678901234), 1234567890)

        isAssert(initMoneyAmountDouble(0.0), 0)
        isAssert(initMoneyAmountDouble(123.0), 123)
        isAssert(initMoneyAmountDouble(0.0), 0)
        isAssert(initMoneyAmountDouble(00.0), 0)
        isAssert(initMoneyAmountDouble(0000.0), 0)
        isAssert(initMoneyAmountDouble(0.12), 0)
        isAssert(initMoneyAmountDouble(00.12), 0)
        isAssert(initMoneyAmountDouble(0000.12), 0)
        isAssert(initMoneyAmountDouble(0000.1200), 0)
        isAssert(initMoneyAmountDouble(5.123), 5)
        isAssert(initMoneyAmountDouble(6.1234), 6)
        isAssert(initMoneyAmountDouble(123456.1234), 123456)
        isAssert(initMoneyAmountDouble(12345678.1234), 12345678)
        isAssert(initMoneyAmountDouble(1234567890.1234), 1234567890)


        isAssert(initMoneyAmountString("0"), 0)
        isAssert(initMoneyAmountString("123"), 123)
        isAssert(initMoneyAmountString("012"), 12)
        isAssert(initMoneyAmountString("012").toString(), "12")
        isAssert(initMoneyAmountString("000012").toString(), "12")
        isAssert(initMoneyAmountString("0040012").toString(), "40012")
        isAssert(initMoneyAmountString("5123"), 5123)
        isAssert(initMoneyAmountString("123456"), 123456)
        isAssert(initMoneyAmountString("1234567890"), 1234567890)
        isAssert(initMoneyAmountString("12345678901234"), 1234567890)
        isAssert(initMoneyAmountString("0"), 0)
        isAssert(initMoneyAmountString("123.0"), 123)
        isAssert(initMoneyAmountString("0.0"), 0)
        isAssert(initMoneyAmountString("0.12"), 0)
        isAssert(initMoneyAmountString("5.123"), 5)
        isAssert(initMoneyAmountString("6.1234"), 6)
        isAssert(initMoneyAmountString("123456.1234"), 123456)
        isAssert(initMoneyAmountString("12345678.1234"), 12345678)
        isAssert(initMoneyAmountString("1234567890.1234"), 1234567890)
        isAssert(initMoneyAmountString("0ds&#\$%!"), 0)
        isAssert(initMoneyAmountString("ds&#\$%!1ds&#\$%!23ds&#\$%!"), 123)
        isAssert(initMoneyAmountString("0ds&#\$%!12ds&#\$%!"), 12)
        isAssert(initMoneyAmountString("012ds&#\$%!").toString(), "12")
        isAssert(initMoneyAmountString("0000ds&#\$%!12").toString(), "12")
        isAssert(initMoneyAmountString("004ds&#\$%!0012").toString(), "40012")
        isAssert(initMoneyAmountString("5ds&#\$%!123"), 5123)
        isAssert(initMoneyAmountString("123456vds&#\$%!"), 123456)
        isAssert(initMoneyAmountString("1ds&#\$%!234567890"), 1234567890)
        isAssert(initMoneyAmountString("ds&#\$%!12345678ds&#\$%!901234"), 1234567890)
        isAssert(initMoneyAmountString("0"), 0)
        isAssert(initMoneyAmountString("1ds&#\$%!23.0"), 123)
        isAssert(initMoneyAmountString("ds&#\$%!0.0ds&#\$%!"), 0)
        isAssert(initMoneyAmountString("ds&#\$%!0.12"), 0)
        isAssert(initMoneyAmountString("5.12ds&#\$%!3"), 5)
        isAssert(initMoneyAmountString("ds&#\$%!6.1234"), 6)
        isAssert(initMoneyAmountString("ds&#\$%!12ds&#\$%!3456.1234"), 123456)
        isAssert(initMoneyAmountString("ds&#\$%!1234567ds&#\$%!8.1234"), 12345678)
        isAssert(initMoneyAmountString("ds&#\$%!12345678ds&#\$%!90.1234"), 1234567890)


        isAssert(_getMoneyFormatted("0"), "0 ${kzt}")
        isAssert(_getMoneyFormatted("123"), "123 ${kzt}")
        isAssert(_getMoneyFormatted("012"), "12 ${kzt}")
        isAssert(_getMoneyFormatted("012"), "12 ${kzt}")
        isAssert(_getMoneyFormatted("000012"), "12 ${kzt}")
        isAssert(_getMoneyFormatted("0040012"), "40 012 ${kzt}")
        isAssert(_getMoneyFormatted("5123"), "5 123 ${kzt}")
        isAssert(_getMoneyFormatted("123456"), "123 456 ${kzt}")
        isAssert(_getMoneyFormatted("1234567890"), "1 234 567 890 ${kzt}")
        isAssert(_getMoneyFormatted("12345678901234"), "1 234 567 890 ${kzt}")
        isAssert(_getMoneyFormatted("123.0"), "123 ${kzt}")
        isAssert(_getMoneyFormatted("0.0"), "0 ${kzt}")
        isAssert(_getMoneyFormatted("0.12"), "0 ${kzt}")
        isAssert(_getMoneyFormatted("5.123"), "5 ${kzt}")
        isAssert(_getMoneyFormatted("6.1234"), "6 ${kzt}")
        isAssert(_getMoneyFormatted("123456.1234"), "123 456 ${kzt}")
        isAssert(_getMoneyFormatted("12345678.1234"), "12 345 678 ${kzt}")
        isAssert(_getMoneyFormatted("1234567890.1234"), "1 234 567 890 ${kzt}")
        isAssert(_getMoneyFormatted("0ds&#\$%!"), "0 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!1ds&#\$%!23ds&#\$%!"), "123 ${kzt}")
        isAssert(_getMoneyFormatted("0ds&#\$%!12ds&#\$%!"), "12 ${kzt}")
        isAssert(_getMoneyFormatted("012ds&#\$%!").toString(), "12 ${kzt}")
        isAssert(_getMoneyFormatted("0000ds&#\$%!12").toString(), "12 ${kzt}")
        isAssert(_getMoneyFormatted("004ds&#\$%!0012").toString(), "40 012 ${kzt}")
        isAssert(_getMoneyFormatted("5ds&#\$%!123"), "5 123 ${kzt}")
        isAssert(_getMoneyFormatted("123456vds&#\$%!"), "123 456 ${kzt}")
        isAssert(_getMoneyFormatted("1ds&#\$%!234567890"), "1 234 567 890 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!12345678ds&#\$%!901234"), "1 234 567 890 ${kzt}")
        isAssert(_getMoneyFormatted("1ds&#\$%!23.0"), "123 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!0.0ds&#\$%!"), "0 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!0.12"), "0 ${kzt}")
        isAssert(_getMoneyFormatted("5.12ds&#\$%!3"), "5 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!6.1234"), "6 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!12ds&#\$%!3456.1234"), "123 456 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!1234567ds&#\$%!8.1234"), "12 345 678 ${kzt}")
        isAssert(_getMoneyFormatted("ds&#\$%!12345678ds&#\$%!90.1234"), "1 234 567 890 ${kzt}")
    }

    private fun initMoneyAmountString(amounts: String): Long {
        return Money.initString(amounts).amount
    }

    private fun initMoneyAmountDouble(amounts: Double): Long {
        return Money.initDouble(amounts).amount
    }

    private fun initMoneyAmountMoney(amounts: Money): Long {
        return Money.initMoney(amounts).amount
    }

    private fun initMoneyAmountLong(amount: Long): Long {
        return Money.initLong(amount).amount
    }

    private fun initMoney(amount: String): Money {
        return Money.initMoney(Money(amount.toLong()))
    }

    private fun initMoney(amount: Long): Money {
        return Money.initMoney(Money(amount))
    }

    private fun _getMoneyFormatted(amount: String): String {
        return getMoneyFormatted(amount)
    }
}

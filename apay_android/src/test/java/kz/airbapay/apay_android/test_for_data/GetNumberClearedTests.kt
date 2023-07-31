package kz.airbapay.apay_android.test_for_data

import kz.airbapay.apay_android.data.constant.kzt
import kz.airbapay.apay_android.data.utils.getNumberCleared
import kz.airbapay.apay_android.isAssert
import org.junit.Test

class GetNumberClearedTests {

    private fun testGetNumberCleared(
        amount: String?,
        isUserEntered: Boolean = false
    ): String {
        return getNumberCleared(
            amount,
            isUserEntered = isUserEntered
        )
    }

    @Test
    fun testsGetNumberCleared() {

        isAssert(testGetNumberCleared("±§!@#\$&*:[]`~%^&100000.0"), "100000")
        isAssert(testGetNumberCleared("100000&*()^.0"), "100000")
        isAssert(testGetNumberCleared("100000&*()^,0"), "100000")
        isAssert(testGetNumberCleared("100000&*()^.00"), "100000")
        isAssert(testGetNumberCleared("100000&*()^,00"), "100000")
        isAssert(testGetNumberCleared("100000&*()^"), "100000")
        isAssert(testGetNumberCleared("1000&*()^00"), "100000")

        isAssert(
            testGetNumberCleared("±§!@#\$&*:[]`~%^&1000000", isUserEntered = true),
            "1000000"
        )
        isAssert(testGetNumberCleared("100000&*()^.0", isUserEntered = true), "1000000")
        isAssert(testGetNumberCleared("100000&*()^,0", isUserEntered = true), "1000000")
        isAssert(testGetNumberCleared("100000&*()^.00", isUserEntered = true), "10000000")
        isAssert(testGetNumberCleared("100000&*()^,00", isUserEntered = true), "10000000")
        isAssert(testGetNumberCleared("100000&*()^", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("1000&*()^00", isUserEntered = true), "100000")

        isAssert(testGetNumberCleared(" 1 234 567 890 ${kzt}", isUserEntered = true), "1234567890")
        isAssert(
            testGetNumberCleared(" 1 234 567 890 ${kzt}", isUserEntered = false),
            "1234567890"
        )

        testWithMinus()
    }

    private fun testWithMinus() {
        isAssert(testGetNumberCleared("-100000"), "-100000")
        isAssert(testGetNumberCleared("--100000"), "-100000")
        isAssert(testGetNumberCleared("--100-000"), "-100000")
        isAssert(testGetNumberCleared("-100000.0"), "-100000")
        isAssert(testGetNumberCleared("-1000zXьЛ00"), "-100000")
        isAssert(testGetNumberCleared("1-000zXьЛ00"), "100000")
        isAssert(testGetNumberCleared("1000-zXьЛ00"), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ-00"), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00-"), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00.-"), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00.-0"), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00.-00"), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00.-0-0"), "100000")
        isAssert(testGetNumberCleared("-1000zXьЛ00.-0-0"), "-100000")

        isAssert(testGetNumberCleared("-100000", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("--100000", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("--100-000", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("-100000.0", isUserEntered = true), "1000000")
        isAssert(testGetNumberCleared("-1000zXьЛ00", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("1-000zXьЛ00", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("1000-zXьЛ00", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ-00", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00-", isUserEntered = true), "100000")
        isAssert(testGetNumberCleared("1000zXьЛ00.-", isUserEntered = true), "100000")
        isAssert(
            testGetNumberCleared("1000zXьЛ00.-0", isUserEntered = true),
            "1000000"
        )
        isAssert(
            testGetNumberCleared("1000zXьЛ00.-00", isUserEntered = true),
            "10000000"
        )
        isAssert(
            testGetNumberCleared("1000zXьЛ00.-0-0", isUserEntered = true),
            "10000000"
        )
        isAssert(
            testGetNumberCleared("-1000zXьЛ00.-0-0", isUserEntered = true),
            "10000000"
        )

    }
}
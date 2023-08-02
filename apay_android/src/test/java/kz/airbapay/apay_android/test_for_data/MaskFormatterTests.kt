package kz.airbapay.apay_android.test_for_data

import kz.airbapay.apay_android.data.utils.MaskUtils
import kz.airbapay.apay_android.data.utils.getNumberCleared
import kz.airbapay.apay_android.isAssert
import org.junit.Test

class MaskFormatterTests {

    @Test
    fun testMaskFormatter() {

        testPhone("+7 (705) 123-45-67")
        testPhone("8 (705) 123-45-67")
        testPhone("+7(705)123-45-67")
        testPhone("+7705123-45-67")
        testPhone("+77051234567")
        testPhone("7 (705) 123-45-67")

        testPhone("7051234567")
        testPhone("+ (705) 123-45-67")

        isAssert(initMaskFormatterAndFormat("XXXXXAAAA", "S1234567B"), "XXXXXS123")
        isAssert(
            initMaskFormatterAndFormat("+7 (AAA) AAA AA AA", "7051234567"),
            "+7 (705) 123 45 67"
        )
        isAssert(
            initMaskFormatterAndFormat("+7 (AAA) AAA AA AA", "7051234567"),
            "+7 (705) 123 45 67"
        )
        isAssert(
            initMaskFormatterAndFormat("+7 (AAA)-AAA-AA-AA", "7051234567"),
            "+7 (705)-123-45-67"
        )
        isAssert(
            initMaskFormatterAndFormat("AAAA*AAAA*AAAA*AAAA", "1234567890123456"),
            "1234*5678*9012*3456"
        )
        isAssert(
            initMaskFormatterAndFormat("AAAA AAAA AAAA AAAA", "1234567890123456"),
            "1234 5678 9012 3456"
        )

        isAssert(initNewCursorPosition( 0), 0)

        isAssert(initNewCursorPosition( 1), 1)
        isAssert(initNewCursorPosition( 2), 2)
        isAssert(initNewCursorPosition( 3), 3)
        isAssert(initNewCursorPosition( 4), 5)

        isAssert(initNewCursorPosition( 5), 5)
        isAssert(initNewCursorPosition( 6), 6)
        isAssert(initNewCursorPosition( 7), 7)
        isAssert(initNewCursorPosition( 8), 8)

        isAssert(initNewCursorPosition( 9), 10)
        isAssert(initNewCursorPosition( 10), 10)
        isAssert(initNewCursorPosition( 11), 11)
        isAssert(initNewCursorPosition( 12), 12)
        isAssert(initNewCursorPosition( 13), 13)

        isAssert(initNewCursorPosition( 14), 15)
    }

    private fun testPhone(text: String) {
        val maskFormatterPhone = initMaskFormatter("+7 (AAA)-AAA-AA-AA")

        val cleared = getNumberCleared(text, isPhoneNumber = true)
        val phone = maskFormatterPhone.format(cleared, true)

        isAssert(phone, "+7 (705)-123-45-67")
    }

    private fun initMaskFormatterAndFormat(
        pattern: String,
        text: String
    ) = initMaskFormatter(pattern).format(text, true)


    private fun initMaskFormatter(
        pattern: String
    ) = MaskUtils(pattern)

    private fun initNewCursorPosition(
        newPosition: Int,
        mask: String = "AAAA AAAA AAAA AAAA",
    ): Int {
        val maskUtils = MaskUtils(mask)
        return maskUtils.getNextCursorPosition(newPosition)
    }
}

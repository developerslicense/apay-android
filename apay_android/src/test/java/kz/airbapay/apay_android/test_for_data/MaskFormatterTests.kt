package kz.airbapay.apay_android.test_for_data

import kz.airbapay.apay_android.data.utils.MaskFormatter
import kz.airbapay.apay_android.data.utils.getNumberCleared
import kz.airbapay.apay_android.isAssert
import org.junit.Test

class MaskFormatterTests {

    @Test
    fun testMaskFormatter() {

            testPhone( "+7 (705) 123-45-67")
            testPhone( "8 (705) 123-45-67")
            testPhone( "+7(705)123-45-67")
            testPhone( "+7705123-45-67")
            testPhone( "+77051234567")
            testPhone( "7 (705) 123-45-67")

            testPhone( "7051234567")
            testPhone( "+ (705) 123-45-67")

             isAssert(initMaskFormatterAndFormat( "XXXXXAAAA",  "S1234567B"),  "XXXXXS123")
             isAssert(
                initMaskFormatterAndFormat( "+7 (AAA) AAA AA AA",  "7051234567"),
                 "+7 (705) 123 45 67"
            ) 
             isAssert(
                initMaskFormatterAndFormat( "+7 (AAA) AAA AA AA",  "7051234567"),
                 "+7 (705) 123 45 67"
            ) 
             isAssert(
                initMaskFormatterAndFormat( "+7 (AAA)-AAA-AA-AA",  "7051234567"),
                 "+7 (705)-123-45-67"
            ) 
             isAssert(
                initMaskFormatterAndFormat( "AAAA*AAAA*AAAA*AAAA",  "1234567890123456"),
                 "1234*5678*9012*3456"
            ) 
             isAssert(
                initMaskFormatterAndFormat( "AAAA AAAA AAAA AAAA",  "1234567890123456"),
                 "1234 5678 9012 3456"
            ) 

        /*     isAssert(_initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "",  "1", 0, 1), 1)
             isAssert(_initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234",  "5123 4", 0, 1), 1)
             isAssert(_initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234",  "1234 5", 4, 5), 6)
             isAssert(_initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5",  "1234 56", 6, 7), 8)
             isAssert(_initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 56",  "1234 567", 7, 8), 9)
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 567",  "1234 5678", 8, 9),
                10
            ) 

             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 0, 1),
                1
            ) 
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 1, 2),
                2
            ) 
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 2, 3),
                3
            ) 
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 3, 4),
                4
            ) 

             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 4, 5),
                6
            ) //! плюс один отступ
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 5, 6),
                7
            ) 
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 6, 7),
                8
            ) 
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 7, 8),
                9
            ) 
             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 8, 9),
                10
            ) 

             isAssert(
                _initNewCursorPosition( "AAAA AAAA AAAA AAAA",  "1234 5678",  "1234 5678 9", 9, 10),
                12
            ) */
    }

    private fun testPhone(text: String) {
        val maskFormatterPhone = initMaskFormatter( "+7 (AAA)-AAA-AA-AA")

        val cleared = getNumberCleared(text, isPhoneNumber = true)
        val phone = maskFormatterPhone.format(cleared)

        isAssert(phone,  "+7 (705)-123-45-67")
    }

    private fun initMaskFormatterAndFormat(
        pattern: String,
        text: String
    ) = initMaskFormatter(pattern).format(text)


    private fun initMaskFormatter(
        pattern: String
    ) = MaskFormatter(pattern)

}

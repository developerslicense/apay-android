package kz.airbapay.apay_android.test_for_card

import kz.airbapay.apay_android.data.utils.card_utils.validateCardNumWithLuhnAlgorithm
import kz.airbapay.apay_android.isAssert
import org.junit.Test

internal class CardLuhnAlgorithmTest {
    @Test
    fun testLuhnAlgorithm() {
        // success
        isAssert(_validateCardNumWithLuhnAlgorithm("5392150002388575"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("4111111111111111"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("6250941006528599"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("6011000991300009"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("5425233430109903"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("2222420000001113"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("4263982640269299"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("4917484589897107"), true)
        isAssert(_validateCardNumWithLuhnAlgorithm("4001919257537193"), true)

        isAssert(_validateCardNumWithLuhnAlgorithm("378282246310005"), true)

        // failure
        isAssert(_validateCardNumWithLuhnAlgorithm("41111111111111"), false)
        isAssert(_validateCardNumWithLuhnAlgorithm("411111111111112"), false)
        isAssert(_validateCardNumWithLuhnAlgorithm("411111111111113"), false)
        isAssert(_validateCardNumWithLuhnAlgorithm("5392250002388575"), false)
        isAssert(_validateCardNumWithLuhnAlgorithm("5392150502388575"), false)
        isAssert(_validateCardNumWithLuhnAlgorithm("5392150002318575"), false)
        isAssert(_validateCardNumWithLuhnAlgorithm("5392150802388575"), false)

    }

    private fun _validateCardNumWithLuhnAlgorithm(
        number: String?
    ) = validateCardNumWithLuhnAlgorithm(number)
}

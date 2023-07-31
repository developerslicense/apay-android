package kz.airbapay.apay_android.data.utils

import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.input.OffsetMapping
import androidx.compose.ui.text.input.TransformedText
import androidx.compose.ui.text.input.VisualTransformation

/// MaskFormatter("XXXXXAAAA").format("S1234567B") // XXXXX567B
/// MaskFormatter("XX.XXX.AAAA").format("S1234567B") // XX.XXX.567B
/// MaskFormatter("**.***.AAAA").format("S1234567B") // **.***.567B
/// MaskFormatter("AA-AAA-AAAA").format("123456789") // 12-345-6789

class MaskFormatter: VisualTransformation {

    private val separator: String = " "

    override fun filter(text: AnnotatedString): TransformedText {
        val money = Money.initString(text.text)
        val formattedText = money.getFormatted()

        return formatTextWithOffset(formattedText)
    }

    private fun formatTextWithOffset(
        formattedText: String
    ): TransformedText {
        val offsetMapping = object : OffsetMapping {

            override fun originalToTransformed(offset: Int): Int {
                val result = calculateOffset(formattedText, offset)
                return if (result < 0 || formattedText.isBlank()) 0 else result
            }

            override fun transformedToOriginal(offset: Int): Int {
                val result = calculateOffset(formattedText, offset)
                return if (result < 0 || formattedText.isBlank()) 0 else result
            }
        }

        return TransformedText(
            text = AnnotatedString(formattedText),
            offsetMapping = offsetMapping
        )
    }

    private fun calculateOffset(
        formattedText: String,
        offset: Int
    ): Int {
        val countOfSeparator = formattedText.count { it.toString() == separator }
        return offset + countOfSeparator + getSpecificOffset(formattedText) - offsetForCyrillicLangSpaceSeparator()
    }

    private fun offsetForCyrillicLangSpaceSeparator() = 1

    private fun getSpecificOffset(formattedText: String) = when {
        formattedText.length > 29 -> 7
        formattedText.length > 25 -> 6
        formattedText.length > 22 -> 5
        formattedText.length > 17 -> 4
        formattedText.length > 13 -> 3
        formattedText.length > 10 -> 2
        formattedText.length > 5 -> 1
        else -> 0
    }
}


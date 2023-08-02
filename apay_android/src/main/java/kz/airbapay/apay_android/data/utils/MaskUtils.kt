package kz.airbapay.apay_android.data.utils

import java.lang.StringBuilder

/// MaskFormatter("XXXXXAAAA").format("S1234567B") // XXXXX567B
/// MaskFormatter("XX.XXX.AAAA").format("S1234567B") // XX.XXX.567B
/// MaskFormatter("**.***.AAAA").format("S1234567B") // **.***.567B
/// MaskFormatter("AA-AAA-AAAA").format("123456789") // 12-345-6789

class MaskUtils(
    private val pattern: String,
    private val isDateExpiredMask: Boolean = false
) {
    private val patternArr = mutableListOf<String>()

    fun getNextCursorPosition(
        newPosition: Int
    ) = try {

        if (pattern[newPosition].toString() != "A") {
            var tempPosition = newPosition + 1
            while (pattern[tempPosition].toString() != "A") {
                tempPosition += 1
            }

            tempPosition
        } else {
            newPosition
        }
    } catch (e: Exception) {
        e.printStackTrace()
        newPosition
    }

    fun format(
        text: String,
        optionForTest: Boolean = false
    ): String {

        val textArr = mutableListOf<String>()
        var textI = 0

        if (optionForTest) {
            patternArr.clear()
        }

        if (patternArr.isEmpty()
            || optionForTest) {
            pattern
                .split("")
                .forEach { ch -> patternArr.add(ch) }
        }

        if (isDateExpiredMask
            && text.isNotBlank()
            && text.first().toString() != "1"
            && text.first().toString() != "0") {

            textArr.add("0")
        }

        text
            .split("")
            .forEach { ch -> textArr.add(ch) }

        try {
            for (patternI in 0 until patternArr.size) {
                if (patternArr[patternI] == "A" && textI < textArr.size) {
                    textI++
                    patternArr[patternI] = textArr[textI]

                } else {
                    continue
                }
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        val sb = StringBuilder()
        patternArr.forEach {
            sb.append(it.replace("A", ""))
        }

        return sb.toString()
    }
}


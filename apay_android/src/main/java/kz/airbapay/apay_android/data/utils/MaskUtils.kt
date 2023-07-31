package kz.airbapay.apay_android.data.utils

import java.lang.StringBuilder

/// MaskFormatter("XXXXXAAAA").format("S1234567B") // XXXXX567B
/// MaskFormatter("XX.XXX.AAAA").format("S1234567B") // XX.XXX.567B
/// MaskFormatter("**.***.AAAA").format("S1234567B") // **.***.567B
/// MaskFormatter("AA-AAA-AAAA").format("123456789") // 12-345-6789

class MaskFormatter(
    private val pattern: String
) {

    fun format(
        text: String
    ): String {
        val patternArr = mutableListOf<String>()
        val textArr = mutableListOf<String>()
        var textI = 0

        pattern
            .split("")
            .forEach { ch -> patternArr.add(ch) }

        text
            .split("")
            .forEach { ch -> textArr.add(ch) }

        for (patternI in 0 until patternArr.size) {
            if (patternArr[patternI] == "A" && textI < textArr.size) {
                textI++
                patternArr[patternI] = textArr[textI]

            } else {
                continue
            }
        }

        val sb = StringBuilder()
        patternArr.forEach {
            sb.append(it.replace("A", ""))
        }

        return sb.toString()
    }
}


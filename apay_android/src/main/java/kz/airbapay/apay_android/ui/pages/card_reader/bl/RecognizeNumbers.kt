package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.graphics.Bitmap

internal class RecognizeNumbers(
    private val image: Bitmap,
    numRows: Int,
    numCols: Int
) {
    private val recognizedDigits: Array<Array<RecognizedDigits?>>

    init {
        recognizedDigits = Array(numRows) { arrayOfNulls(numCols) }
    }

    fun number(model: RecognizedDigitsModel, lines: ArrayList<ArrayList<DetectedBox>>): String? {
        for (line in lines) {
            val candidateNumber = StringBuilder()
            for (word in line) {
                val recognized = cachedDigits(model, word) ?: return null
                candidateNumber.append(recognized.stringResult())
            }
            if (candidateNumber.length == 16) {
                return candidateNumber.toString()
            }
        }
        return null
    }

    private fun cachedDigits(model: RecognizedDigitsModel, box: DetectedBox): RecognizedDigits? {
        if (recognizedDigits[box.row][box.col] == null) {
            recognizedDigits[box.row][box.col] = RecognizedDigits.from(model, image, box.rect)
        }
        return recognizedDigits[box.row][box.col]
    }
}
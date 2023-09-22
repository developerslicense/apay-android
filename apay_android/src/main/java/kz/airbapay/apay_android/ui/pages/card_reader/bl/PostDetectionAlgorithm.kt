package kz.airbapay.apay_android.ui.pages.card_reader.bl

import java.util.Collections

/**
 * Organize the boxes to find possible numbers.
 *
 *
 * After running detection, the post processing algorithm will try to find
 * sequences of boxes that are plausible card numbers. The basic techniques
 * that it uses are non-maximum suppression and depth first search on box
 * sequences to find likely numbers. There are also a number of heuristics
 * for filtering out unlikely sequences.
 */
internal class PostDetectionAlgorithm(boxes: ArrayList<DetectedBox>, findFour: FindFourModel) {
    private val kDeltaRowForCombine = 2
    private val kDeltaColForCombine = 2
    private val sortedBoxes: ArrayList<DetectedBox?>
    private val numRows: Int
    private val numCols: Int

    init {
        numCols = findFour.cols
        numRows = findFour.rows
        sortedBoxes = ArrayList()

        boxes.sort()
        boxes.reverse()

        for (box in boxes) {
            val kMaxBoxesToDetect = 20
            if (sortedBoxes.size >= kMaxBoxesToDetect) {
                break
            }
            sortedBoxes.add(box)
        }
    }

    fun horizontalNumbers(): ArrayList<ArrayList<DetectedBox?>> {
        val boxes = combineCloseBoxes(
            kDeltaRowForCombine,
            kDeltaColForCombine
        )

        val kNumberWordCount = 4
        val lines = findHorizontalNumbers(boxes, kNumberWordCount)
        val linesOut = ArrayList<ArrayList<DetectedBox?>>()
        // boxes should be roughly evenly spaced, reject any that aren't

        for (line in lines) {
            val deltas = ArrayList<Int>()
            for (idx in 0 until line.size - 1) {
                deltas.add(line[idx + 1]!!.col - line[idx]!!.col)
            }

            deltas.sort()

            val maxDelta = deltas[deltas.size - 1]
            val minDelta = deltas[0]
            if (maxDelta - minDelta <= 2) {
                linesOut.add(line)
            }
        }
        return linesOut
    }

    fun verticalNumbers(): ArrayList<ArrayList<DetectedBox?>> {
        val boxes = combineCloseBoxes(
            kDeltaRowForCombine,
            kDeltaColForCombine
        )
        val lines = findVerticalNumbers(boxes)
        val linesOut = ArrayList<ArrayList<DetectedBox?>>()

        // boxes should be roughly evenly spaced, reject any that aren't
        for (line in lines) {
            val deltas = ArrayList<Int>()

            for (idx in 0 until line.size - 1) {
                deltas.add(line[idx + 1]!!.row - line[idx]!!.row)
            }
            deltas.sort()

            val maxDelta = deltas[deltas.size - 1]
            val minDelta = deltas[0]
            if (maxDelta - minDelta <= 2) {
                linesOut.add(line)
            }
        }
        return linesOut
    }

    private fun horizontalPredicate(currentWord: DetectedBox, nextWord: DetectedBox?): Boolean {
        val kDeltaRowForHorizontalNumbers = 1
        return nextWord!!.col > currentWord.col && nextWord.row >= currentWord.row - kDeltaRowForHorizontalNumbers && nextWord.row <= currentWord.row + kDeltaRowForHorizontalNumbers
    }

    private fun verticalPredicate(currentWord: DetectedBox, nextWord: DetectedBox?): Boolean {
        val kDeltaColForVerticalNumbers = 1
        return nextWord!!.row > currentWord.row && nextWord.col >= currentWord.col - kDeltaColForVerticalNumbers && nextWord.col <= currentWord.col + kDeltaColForVerticalNumbers
    }

    private fun findNumbers(
        currentLine: ArrayList<DetectedBox?>, words: ArrayList<DetectedBox?>,
        useHorizontalPredicate: Boolean, numberOfBoxes: Int,
        lines: ArrayList<ArrayList<DetectedBox?>>
    ) {
        if (currentLine.size == numberOfBoxes) {
            lines.add(currentLine)
            return
        }
        if (words.size == 0) {
            return
        }

        val currentWord = currentLine[currentLine.size - 1] ?: return

        for (idx in words.indices) {
            val word = words[idx]

            if (useHorizontalPredicate && horizontalPredicate(currentWord, word)) {
                val newCurrentLine = ArrayList(currentLine)
                newCurrentLine.add(word)
                findNumbers(
                    newCurrentLine, dropFirst(words, idx + 1), true,
                    numberOfBoxes, lines
                )

            } else if (verticalPredicate(currentWord, word)) {
                val newCurrentLine = ArrayList(currentLine)
                newCurrentLine.add(word)
                findNumbers(
                    newCurrentLine, dropFirst(words, idx + 1), useHorizontalPredicate,
                    numberOfBoxes, lines
                )
            }
        }
    }

    private fun dropFirst(boxes: ArrayList<DetectedBox?>, n: Int): ArrayList<DetectedBox?> {
        val result = ArrayList<DetectedBox?>()
        for (idx in n until boxes.size) {
            result.add(boxes[idx])
        }
        return result
    }

    // Note: this is simple but inefficient. Since we're dealing with small
    // lists (eg 20 items) it should be fine
    private fun findHorizontalNumbers(
        words: ArrayList<DetectedBox?>,
        numberOfBoxes: Int
    ): ArrayList<ArrayList<DetectedBox?>> {
        Collections.sort(words, colCompare)
        val lines = ArrayList<ArrayList<DetectedBox?>>()
        for (idx in words.indices) {
            val currentLine = ArrayList<DetectedBox?>()
            currentLine.add(words[idx])
            findNumbers(
                currentLine, dropFirst(words, idx + 1), true,
                numberOfBoxes, lines
            )
        }
        return lines
    }

    private fun findVerticalNumbers(words: ArrayList<DetectedBox?>): ArrayList<ArrayList<DetectedBox?>> {
        val numberOfBoxes = 4
        Collections.sort(words, rowCompare)
        val lines = ArrayList<ArrayList<DetectedBox?>>()
        for (idx in words.indices) {
            val currentLine = ArrayList<DetectedBox?>()
            currentLine.add(words[idx])
            findNumbers(
                currentLine, dropFirst(words, idx + 1), false,
                numberOfBoxes, lines
            )
        }
        return lines
    }

    /**
     * Combine close boxes favoring high confidence boxes.
     */
    private fun combineCloseBoxes(deltaRow: Int, deltaCol: Int): ArrayList<DetectedBox?> {
        val cardGrid = Array(numRows) { BooleanArray(numCols) }
        for (row in 0 until numRows) {
            for (col in 0 until numCols) {
                cardGrid[row][col] = false
            }
        }
        for (box in sortedBoxes) {
            cardGrid[box!!.row][box.col] = true
        }

        // since the boxes are sorted by confidence, go through them in order to
        // result in only high confidence boxes winning. There are corner cases
        // where this will leave extra boxes, but that's ok because we don't
        // need to be perfect here
        for (box in sortedBoxes) {
            if (!cardGrid[box!!.row][box.col]) {
                continue
            }
            for (row in box.row - deltaRow..box.row + deltaRow) {
                for (col in box.col - deltaCol..box.col + deltaCol) {
                    if (row in 0 until numRows && col >= 0 && col < numCols) {
                        cardGrid[row][col] = false
                    }
                }
            }

            // add this box back
            cardGrid[box.row][box.col] = true
        }
        val combinedBoxes = ArrayList<DetectedBox?>()
        for (box in sortedBoxes) {
            if (cardGrid[box!!.row][box.col]) {
                combinedBoxes.add(box)
            }
        }
        return combinedBoxes
    }

    companion object {
        private val colCompare: Comparator<DetectedBox?> =
            Comparator { o1, o2 ->
                if ((o1?.col ?: 0) < (o2?.col ?: 0)) -1
                else if ((o1?.col ?: 0) == (o2?.col ?: 0)) 0
                else 1
            }
        private val rowCompare: Comparator<DetectedBox?> =
            Comparator { o1, o2 ->
                if ((o1?.row ?: 0) < (o2?.row ?: 0)) -1
                else if ((o1?.row ?: 0) == (o2?.row ?: 0)) 0
                else 1
            }
    }
}
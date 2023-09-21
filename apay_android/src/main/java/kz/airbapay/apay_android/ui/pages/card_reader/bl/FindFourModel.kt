/* Copyright 2018 The TensorFlow Authors. All Rights Reserved.

Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at

  http://www.apache.org/licenses/LICENSE-2.0

Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
==============================================================================*/
package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.content.Context
import java.io.IOException
import java.nio.MappedByteBuffer

/**
 * This classifier works with the float MobileNet model.
 */
internal class FindFourModel(context: Context?) : ImageClassifier(
    context!!
) {
    @JvmField
	val rows = 34
    @JvmField
	val cols = 51
    @JvmField
	val boxSize = CGSize(80f, 36f)
    @JvmField
	val cardSize = CGSize(480f, 302f)

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs. This isn't part
     * of the super class, because we need a primitive array here.
     */
    private val labelProbArray: Array<Array<Array<FloatArray>>>

    /**
     * Initializes an `ImageClassifierFloatMobileNet`.
     */
    init {
        val classes = 3
        labelProbArray = Array(1) { Array(rows) { Array(cols) { FloatArray(classes) } } }
    }

    fun hasDigits(row: Int, col: Int): Boolean {
        return digitConfidence(row, col) >= 0.5
    }

    fun hasExpiry(row: Int, col: Int): Boolean {
        return expiryConfidence(row, col) >= 0.5
    }

    fun digitConfidence(row: Int, col: Int): Float {
        val digitClass = 1
        return labelProbArray[0][row][col][digitClass]
    }

    fun expiryConfidence(row: Int, col: Int): Float {
        val expiryClass = 2
        return labelProbArray[0][row][col][expiryClass]
    }

    @Throws(IOException::class)
    override fun loadModelFile(context: Context?): MappedByteBuffer? {
        return ResourceModelFactory.getInstance().loadFindFourFile(context)
    }

    override fun getImageSizeX() = 480
    override fun getImageSizeY() = 302
    override fun getNumBytesPerChannel() = 4

    override fun addPixelValue(pixelValue: Int) {
        imgData!!.putFloat((pixelValue shr 16 and 0xFF) / 255f)
        imgData!!.putFloat((pixelValue shr 8 and 0xFF) / 255f)
        imgData!!.putFloat((pixelValue and 0xFF) / 255f)
    }

    override fun runInference() {
        tflite!!.run(imgData, labelProbArray)
    }
}
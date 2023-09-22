package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.content.Context
import java.io.IOException
import java.nio.MappedByteBuffer

internal class RecognizedDigitsModel(
    context: Context?
) : ImageClassifier(
    context!!
) {
    private val classes = 11

    /**
     * An array to hold inference results, to be feed into Tensorflow Lite as outputs. This isn't part
     * of the super class, because we need a primitive array here.
     */
    private val labelProbArray: Array<Array<Array<FloatArray>>> = Array(1) {
        Array(1) { Array(kNumPredictions) { FloatArray(classes) } }
    }

    internal inner class ArgMaxAndConfidence(@JvmField val argMax: Int, @JvmField val confidence: Float)

    fun argAndValueMax(col: Int): ArgMaxAndConfidence {
        var maxIdx = -1
        var maxValue = (-1.0).toFloat()
        for (idx in 0 until classes) {
            val value = labelProbArray[0][0][col][idx]
            if (value > maxValue) {
                maxIdx = idx
                maxValue = value
            }
        }
        return ArgMaxAndConfidence(maxIdx, maxValue)
    }

    @Throws(IOException::class)
    override fun loadModelFile(context: Context): MappedByteBuffer? {
        return ResourceModelFactory.instance?.loadRecognizeDigitsFile(context)
    }

    override fun getImageSizeX() = 80
    override fun getImageSizeY() = 36
    override fun getNumBytesPerChannel() = 4

    override fun addPixelValue(pixelValue: Int) {
        imgData!!.putFloat((pixelValue shr 16 and 0xFF) / 255f)
        imgData!!.putFloat((pixelValue shr 8 and 0xFF) / 255f)
        imgData!!.putFloat((pixelValue and 0xFF) / 255f)
    }

    override fun runInference() {
        tflite!!.run(imgData, labelProbArray)
    }

    companion object {
        const val kNumPredictions = 17
    }
}
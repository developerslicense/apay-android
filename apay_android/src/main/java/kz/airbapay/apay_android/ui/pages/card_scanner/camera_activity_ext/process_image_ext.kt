package kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraActivity
import kz.airbapay.apay_android.ui.pages.card_scanner.rectangle_detector.Classifier
import java.util.LinkedList

private const val MINIMUM_CONFIDENCE_TF_OD_API = 0.5f

internal fun CameraActivity.processImage() {
    ++timestamp
    val currTimestamp = timestamp
    trackingOverlay!!.postInvalidate()

    // No mutex needed as this method is not reentrant.
    if (computingDetection) {
        readyForNextImage()
        return
    }
    computingDetection = true
    rgbFrameBitmap!!.setPixels(
        getRgbBytes()!!,
        0,
        previewWidth,
        0,
        0,
        previewWidth,
        previewHeight
    )
    readyForNextImage()
    val canvas = Canvas(croppedBitmap!!)
    canvas.drawBitmap(rgbFrameBitmap!!, frameToCropTransform!!, null)

    runInBackground {
        val results: List<Classifier.Recognition> =
            ArrayList() // todo !!! //detector.recognizeImage(croppedBitmap);
        cropCopyBitmap = Bitmap.createBitmap(croppedBitmap!!)
        val canvas1 = Canvas(cropCopyBitmap!!)
        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 2.0f
        val mappedRecognitions: MutableList<Classifier.Recognition> = LinkedList()
        for (result in results) {
            val location = result.location
            if (location != null && result.confidence >= MINIMUM_CONFIDENCE_TF_OD_API) {
                canvas1.drawRect(location, paint)
                cropToFrameTransform!!.mapRect(location)
                result.location = location
                mappedRecognitions.add(result)
            }
        }
        tracker!!.trackResults(mappedRecognitions, currTimestamp)
        trackingOverlay!!.postInvalidate()
        computingDetection = false
    }
}

@Synchronized
private fun CameraActivity.runInBackground(r: Runnable?) {
    if (handler != null) {
        handler!!.post(r!!)
    }
}

private fun CameraActivity.readyForNextImage() {
    if (postInferenceCallback != null) {
        postInferenceCallback!!.run()
    }
}

private fun CameraActivity.getRgbBytes(): IntArray? {
    imageConverter!!.run()
    return rgbBytes
}
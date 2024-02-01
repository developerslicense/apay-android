package kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.media.Image
import android.media.ImageReader
import android.os.Trace
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraActivity
import kz.airbapay.apay_android.ui.pages.card_scanner.rectangle_detector.Classifier
import kz.airbapay.apay_android.ui.pages.card_scanner.utils.ImageUtils
import java.util.LinkedList

private const val MINIMUM_CONFIDENCE_TF_OD_API = 0.5f


internal fun CameraActivity.onImageAvailableImpl(reader: ImageReader) {
    // We need wait until we have some size from onPreviewSizeChosen
    if (previewWidth == 0 || previewHeight == 0) {
        return
    }
    if (rgbBytes == null) {
        rgbBytes = IntArray(previewWidth * previewHeight)
    }
    try {
        val image = reader.acquireLatestImage() ?: return
        if (isProcessingFrame) {
            image.close()
            return
        }
        isProcessingFrame = true
        Trace.beginSection("imageAvailable")
        val planes = image.planes
        fillBytes(planes, yuvBytes)
        yRowStride = planes[0].rowStride
        val uvRowStride = planes[1].rowStride
        val uvPixelStride = planes[1].pixelStride
        imageConverter = Runnable {
            ImageUtils.convertYUV420ToARGB8888(
                yuvBytes[0],
                yuvBytes[1],
                yuvBytes[2],
                previewWidth,
                previewHeight,
                yRowStride,
                uvRowStride,
                uvPixelStride,
                rgbBytes
            )
        }
        postInferenceCallback = Runnable {
            image.close()
            isProcessingFrame = false
        }
        processImage()
    } catch (e: Exception) {
        e.printStackTrace()
        Trace.endSection()
        return
    }
    Trace.endSection()
}

private fun fillBytes(planes: Array<Image.Plane>, yuvBytes: Array<ByteArray?>) {
    // Because of the variable row stride it's not possible to know in
    // advance the actual necessary dimensions of the yuv planes.
    for (i in planes.indices) {
        val buffer = planes[i].buffer
        if (yuvBytes[i] == null) {
            yuvBytes[i] = ByteArray(buffer.capacity())
        }
        buffer[yuvBytes[i]]
    }
}

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
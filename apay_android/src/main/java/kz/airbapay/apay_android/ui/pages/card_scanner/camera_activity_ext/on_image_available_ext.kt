package kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext

import android.media.Image
import android.media.ImageReader
import android.os.Trace
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraActivity
import kz.airbapay.apay_android.ui.pages.card_scanner.utils.ImageUtils


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
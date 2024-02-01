package kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext

import android.hardware.Camera
import android.util.Size
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraActivity
import kz.airbapay.apay_android.ui.pages.card_scanner.utils.ImageUtils

internal fun CameraActivity.onPreviewFrameImpl(camera: Camera, bytes: ByteArray) {
    try {
        // Initialize the storage bitmaps once when the resolution is known.
        if (rgbBytes == null) {
            val previewSize = camera.parameters.previewSize
            previewHeight = previewSize.height
            previewWidth = previewSize.width
            rgbBytes = IntArray(previewWidth * previewHeight)
            onPreviewSizeChosen(Size(previewSize.width, previewSize.height), 90)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        return
    }
    isProcessingFrame = true
    yuvBytes[0] = bytes
    yRowStride = previewWidth
    imageConverter = Runnable {
        ImageUtils.convertYUV420SPToARGB8888(
            bytes,
            previewWidth,
            previewHeight,
            rgbBytes
        )
    }
    postInferenceCallback = Runnable {
        camera.addCallbackBuffer(bytes)
        isProcessingFrame = false
    }
    processImage()
}

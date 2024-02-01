package kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext

import android.graphics.ImageFormat
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.media.ImageReader
import android.view.Surface
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraConnectionFragment

/**
 * Creates a new [CameraCaptureSession] for camera preview.
 */
internal fun CameraConnectionFragment.createCameraPreviewSession() {
    try {
        val texture = textureView!!.surfaceTexture!!

        // We configure the size of default buffer to be the size of camera preview we want.
        texture.setDefaultBufferSize(previewSize!!.width, previewSize!!.height)

        // This is the output Surface we need to start preview.
        val surface = Surface(texture)

        // We set up a CaptureRequest.Builder with the output Surface.
        previewRequestBuilder = cameraDevice!!.createCaptureRequest(CameraDevice.TEMPLATE_PREVIEW)
        previewRequestBuilder?.addTarget(surface)

        // Create the reader for the preview frames.
        previewReader = ImageReader.newInstance(
            previewSize!!.width, previewSize!!.height, ImageFormat.YUV_420_888, 2
        )
        previewReader!!.setOnImageAvailableListener(imageListener, backgroundHandler)
        previewRequestBuilder!!.addTarget(previewReader!!.surface)

        // Here, we create a CameraCaptureSession for camera preview.
        cameraDevice!!.createCaptureSession(
            listOf(surface, previewReader!!.surface),
            object : CameraCaptureSession.StateCallback() {
                override fun onConfigured(cameraCaptureSession: CameraCaptureSession) {
                    // The camera is already closed
                    if (null == cameraDevice) {
                        return
                    }

                    // When the session is ready, we start displaying the preview.
                    captureSession = cameraCaptureSession
                    try {
                        // Auto focus should be continuous for camera preview.
                        previewRequestBuilder!!.set(
                            CaptureRequest.CONTROL_AF_MODE,
                            CaptureRequest.CONTROL_AF_MODE_CONTINUOUS_PICTURE
                        )
                        // Flash is automatically enabled when necessary.
                        previewRequestBuilder!!.set(
                            CaptureRequest.CONTROL_AE_MODE,
                            CaptureRequest.CONTROL_AE_MODE_ON_AUTO_FLASH
                        )

                        // Finally, we start displaying the camera preview.
                        previewRequest = previewRequestBuilder!!.build()
                        captureSession!!.setRepeatingRequest(
                            previewRequest!!, captureCallback, backgroundHandler
                        )
                    } catch (e: CameraAccessException) {
                        e.printStackTrace()
                    }
                }

                override fun onConfigureFailed(cameraCaptureSession: CameraCaptureSession) {
                    showToast()
                }
            },
            null
        )
    } catch (e: CameraAccessException) {
        e.printStackTrace()
    }
}
package kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext

import android.app.Activity
import android.content.Context
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CameraManager
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraConnectionFragment
import java.util.concurrent.TimeUnit

/**
 * Opens the camera specified by [CameraConnectionFragment.cameraId].
 */
internal fun CameraConnectionFragment.openCamera(width: Int, height: Int) {
    setUpCameraOutputs()
    configureTransform(width, height)
    val activity: Activity? = activity
    val manager = activity!!.getSystemService(Context.CAMERA_SERVICE) as CameraManager

    try {
        if (!cameraOpenCloseLock.tryAcquire(2500, TimeUnit.MILLISECONDS)) {
            throw RuntimeException("Time out waiting to lock camera opening.")
        }
        manager.openCamera(cameraId!!, stateCallback, backgroundHandler)
    } catch (e: Exception) {
        e.printStackTrace()
    }
}

/**
 * Closes the current [CameraDevice].
 */
internal fun CameraConnectionFragment.closeCamera() {
    try {
        cameraOpenCloseLock.acquire()
        if (null != captureSession) {
            captureSession!!.close()
            captureSession = null
        }
        if (null != cameraDevice) {
            cameraDevice!!.close()
            cameraDevice = null
        }
        if (null != previewReader) {
            previewReader!!.close()
            previewReader = null
        }
    } catch (e: InterruptedException) {
        throw RuntimeException("Interrupted while trying to lock camera closing.", e)
    } finally {
        cameraOpenCloseLock.release()
    }
}

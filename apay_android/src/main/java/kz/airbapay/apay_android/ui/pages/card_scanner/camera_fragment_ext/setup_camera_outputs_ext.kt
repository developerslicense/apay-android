package kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext

import android.app.Activity
import android.content.Context
import android.content.res.Configuration
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Size
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraConnectionFragment
import java.util.Collections

/**
 * Sets up member variables related to camera.
 */
internal fun CameraConnectionFragment.setUpCameraOutputs() {
    val activity: Activity = requireActivity()
    val manager = activity.getSystemService(Context.CAMERA_SERVICE) as CameraManager
    try {
        val characteristics = manager.getCameraCharacteristics(cameraId!!)
        val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
        sensorOrientation = characteristics.get(CameraCharacteristics.SENSOR_ORIENTATION)

        // Danger, W.R.! Attempting to use too large a preview size could  exceed the camera
        // bus' bandwidth limitation, resulting in gorgeous previews but the storage of
        // garbage capture data.
        previewSize = chooseOptimalSize(
            if (map != null) map.getOutputSizes(SurfaceTexture::class.java) else arrayOfNulls(0),
            inputSize.width,
            inputSize.height
        )

        // We fit the aspect ratio of TextureView to the size of preview we picked.
        val orientation = resources.configuration.orientation
        if (orientation == Configuration.ORIENTATION_LANDSCAPE) {
            textureView!!.setAspectRatio(previewSize!!.width, previewSize!!.height)
        } else {
            textureView!!.setAspectRatio(previewSize!!.height, previewSize!!.width)
        }
    } catch (e: Exception) {
        e.printStackTrace()
        showToast()
    }
    cameraConnectionCallback.invoke(previewSize!!, sensorOrientation!!)
}

/**
 * Given `choices` of `Size`s supported by a camera, chooses the smallest one whose
 * width and height are at least as large as the minimum of both, or an exact match if possible.
 *
 * @param choices The list of sizes that the camera supports for the intended output class
 * @param width   The minimum desired width
 * @param height  The minimum desired height
 * @return The optimal `Size`, or an arbitrary one if none were big enough
 */
private fun chooseOptimalSize(choices: Array<Size?>, width: Int, height: Int): Size {
    val minSize = Math.max(Math.min(width, height), 320)
    val desiredSize = Size(width, height)

    // Collect the supported resolutions that are at least as big as the preview Surface
    var exactSizeFound = false
    val bigEnough: MutableList<Size> = ArrayList()
    val tooSmall: MutableList<Size> = ArrayList()
    for (option in choices) {
        if (option == desiredSize) {
            // Set the size but don't return yet so that remaining sizes will still be logged.
            exactSizeFound = true
        }
        if (option!!.height >= minSize && option.width >= minSize) {
            bigEnough.add(option)
        } else {
            tooSmall.add(option)
        }
    }
    if (exactSizeFound) {
        return desiredSize
    }

    // Pick the smallest of those, assuming we found any
    return if (bigEnough.size > 0) {
        Collections.min(bigEnough, CompareSizesByArea())
    } else {
        choices[0]!!
    }
}

/**
 * Compares two `Size`s based on their areas.
 */
internal class CompareSizesByArea : Comparator<Size> {
    override fun compare(lhs: Size, rhs: Size): Int {
        // We cast here to ensure the multiplications won't overflow
        return java.lang.Long.signum(
            lhs.width.toLong() * lhs.height - rhs.width.toLong() * rhs.height
        )
    }
}
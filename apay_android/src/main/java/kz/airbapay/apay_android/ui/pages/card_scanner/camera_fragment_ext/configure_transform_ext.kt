package kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext

import android.app.Activity
import android.graphics.Matrix
import android.graphics.RectF
import android.view.Surface
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraConnectionFragment

/**
 * Configures the necessary [Matrix] transformation to `mTextureView`. This method should be
 * called after the camera preview size is determined in setUpCameraOutputs and also the size of
 * `mTextureView` is fixed.
 *
 * @param viewWidth  The width of `mTextureView`
 * @param viewHeight The height of `mTextureView`
 */
internal fun CameraConnectionFragment.configureTransform(viewWidth: Int, viewHeight: Int) {
    val activity: Activity? = activity
    if (null == textureView || null == previewSize || null == activity) {
        return
    }
    val rotation = activity.windowManager.defaultDisplay.rotation
    val matrix = Matrix()
    val viewRect = RectF(0f, 0f, viewWidth.toFloat(), viewHeight.toFloat())
    val bufferRect = RectF(0f, 0f, previewSize!!.height.toFloat(), previewSize!!.width.toFloat())
    val centerX = viewRect.centerX()
    val centerY = viewRect.centerY()

    if (Surface.ROTATION_90 == rotation || Surface.ROTATION_270 == rotation) {
        bufferRect.offset(centerX - bufferRect.centerX(), centerY - bufferRect.centerY())
        matrix.setRectToRect(viewRect, bufferRect, Matrix.ScaleToFit.FILL)
        val scale = Math.max(
            viewHeight.toFloat() / previewSize!!.height,
            viewWidth.toFloat() / previewSize!!.width
        )
        matrix.postScale(scale, scale, centerX, centerY)
        matrix.postRotate((90 * (rotation - 2)).toFloat(), centerX, centerY)

    } else if (Surface.ROTATION_180 == rotation) {
        matrix.postRotate(180f, centerX, centerY)
    }
    textureView!!.setTransform(matrix)
}
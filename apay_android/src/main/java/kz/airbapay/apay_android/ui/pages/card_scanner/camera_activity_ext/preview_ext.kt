package kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Matrix
import android.hardware.camera2.CameraAccessException
import android.hardware.camera2.CameraCharacteristics
import android.hardware.camera2.CameraManager
import android.util.Size
import android.view.Surface
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraActivity
import kz.airbapay.apay_android.ui.pages.card_scanner.rectangle_detector.MultiBoxTracker
import kz.airbapay.apay_android.ui.pages.card_scanner.utils.ImageUtils
import kz.airbapay.apay_android.ui.pages.card_scanner.view.OverlayView

internal fun CameraActivity.onPreviewSizeChosen(
    size: Size,
    rotation: Int
) {
    tracker = MultiBoxTracker(this)
    val cropSize = 300

    /*
    try {
        detector =
                TFLiteObjectDetectionAPIModel.create(
                        getAssets(),
                        TF_OD_API_MODEL_FILE,
                        TF_OD_API_LABELS_FILE,
                        TF_OD_API_INPUT_SIZE,
                        TF_OD_API_IS_QUANTIZED);

    } catch (final IOException e) {
        e.printStackTrace();
        Toast toast =
                Toast.makeText(
                        getApplicationContext(), "Classifier could not be initialized", Toast.LENGTH_SHORT);
        toast.show();
        finish();
    } */
    previewWidth = size.width
    previewHeight = size.height

    val sensorOrientation = rotation - screenOrientation()

    rgbFrameBitmap = Bitmap.createBitmap(previewWidth, previewHeight, Bitmap.Config.ARGB_8888)
    croppedBitmap = Bitmap.createBitmap(cropSize, cropSize, Bitmap.Config.ARGB_8888)

    frameToCropTransform = ImageUtils.getTransformationMatrix(
        previewWidth,
        previewHeight,
        cropSize,
        cropSize,
        sensorOrientation,
        false
    )

    cropToFrameTransform = Matrix()
    frameToCropTransform?.invert(cropToFrameTransform)
    trackingOverlay = findViewById<View>(R.id.tracking_overlay) as OverlayView
    trackingOverlay!!.addCallback { canvas: Canvas? -> tracker!!.draw(canvas) }
    tracker!!.setFrameConfiguration(previewWidth, previewHeight, sensorOrientation)
}

private fun CameraActivity.screenOrientation() = when (windowManager.defaultDisplay.rotation) {
    Surface.ROTATION_270 -> 270
    Surface.ROTATION_180 -> 180
    Surface.ROTATION_90 -> 90
    else -> 0
}

internal fun CameraActivity.chooseCamera(): String? {
    val manager = getSystemService(AppCompatActivity.CAMERA_SERVICE) as CameraManager
    try {
        for (cameraId in manager.cameraIdList) {
            val characteristics = manager.getCameraCharacteristics(cameraId)

            // We don't use a front facing camera in this sample.
            val facing = characteristics.get(CameraCharacteristics.LENS_FACING)
            if (facing != null && facing == CameraCharacteristics.LENS_FACING_FRONT) {
                continue
            }
            val map = characteristics.get(CameraCharacteristics.SCALER_STREAM_CONFIGURATION_MAP)
                ?: continue
            return cameraId
        }
    } catch (e: CameraAccessException) {
        e.printStackTrace()
    }
    return null
}
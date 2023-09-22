package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.hardware.Camera
import android.os.SystemClock
import android.util.Log
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.widget.FrameLayout
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity
import java.io.IOException

internal class ExecutorCamera(
    private val activity: ScanActivity,
    private val tryAcquire: () -> Boolean
) : Camera.PreviewCallback, OnCameraOpenListener {

    var mRoiCenterYRatio = 0f
    // set when this activity posts to the machineLearningThread
    private var mPredictionStartMs: Long = 0
    var mIsPermissionCheckDone = false
    private var mCamera: Camera? = null
    private var mRotation = 0
    private var mCameraThread: CameraThread? = null

    fun onPause() {
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.release()
            mCamera = null
        }
    }

    override fun onCameraOpen(camera: Camera?) {
        if (camera == null) {
            val intent = Intent()
            intent.putExtra(ScanActivity.RESULT_CAMERA_OPEN_ERROR, true)
            activity.setResult(Activity.RESULT_CANCELED, intent)
            activity.finish()
        } else if (!activity.executorML!!.mIsActivityActive) {
            camera.release()
        } else {
            mCamera = camera
            setCameraDisplayOrientation(
                activity,
                Camera.CameraInfo.CAMERA_FACING_BACK,
                mCamera!!
            )
            // Create our Preview view and set it as the content of our activity.
            val cameraPreview = CameraPreview(activity, this)
            val preview = activity.findViewById<FrameLayout>(R.id.texture)
            preview.addView(cameraPreview)
            mCamera!!.setPreviewCallback(this)
        }
    }

    fun startCamera() {
        try {
            if (mIsPermissionCheckDone) {
                if (mCameraThread == null) {
                    mCameraThread = CameraThread()
                    mCameraThread!!.start()
                }
                mCameraThread!!.startCamera(this)
            }
        } catch (e: Exception) {
            val builder = AlertDialog.Builder(activity)
            val dialog = builder.create()
            dialog.show()
        }
    }

    private fun setCameraDisplayOrientation(
        activity: Activity,
        cameraId: Int, camera: Camera
    ) {
        val info = Camera.CameraInfo()
        Camera.getCameraInfo(cameraId, info)

        val rotation = activity.windowManager.defaultDisplay.rotation
        var degrees = 0

        when (rotation) {
            Surface.ROTATION_0 -> degrees = 0
            Surface.ROTATION_90 -> degrees = 90
            Surface.ROTATION_180 -> degrees = 180
            Surface.ROTATION_270 -> degrees = 270
        }

        var result: Int
        if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            result = (info.orientation + degrees) % 360
            result = (360 - result) % 360 // compensate the mirror
        } else {  // back-facing
            result = (info.orientation - degrees + 360) % 360
        }

        camera.setDisplayOrientation(result)
        mRotation = result
    }

    override fun onPreviewFrame(bytes: ByteArray, camera: Camera) {
        if (tryAcquire()) {
            val mlThread = ScanActivity.machineLearningThread
            val parameters = camera.parameters
            val width = parameters.previewSize.width
            val height = parameters.previewSize.height
            mPredictionStartMs = SystemClock.uptimeMillis()

            // Use the application context here because the machine learning thread's lifecycle
            // is connected to the application and not this activity
            mlThread!!.post(
                bytes, width, height, mRotation, activity.executorML,
                activity.applicationContext, mRoiCenterYRatio
            )
        }
    }

    /**
     * A basic Camera preview class
     */
    inner class CameraPreview(
        context: Context?,
        private val mPreviewCallback: Camera.PreviewCallback
    ) : SurfaceView(context), Camera.AutoFocusCallback, SurfaceHolder.Callback {

        private val mHolder: SurfaceHolder = holder

        init {

            // Install a SurfaceHolder.Callback so we get notified when the
            // underlying surface is created and destroyed.
            mHolder.addCallback(this)
            // deprecated setting, but required on Android versions prior to 3.0
            mHolder.setType(SurfaceHolder.SURFACE_TYPE_PUSH_BUFFERS)

            val params = mCamera!!.parameters
            val focusModes = params.supportedFocusModes

            if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE)) {
                params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_PICTURE
            } else if (focusModes.contains(Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO)) {
                params.focusMode = Camera.Parameters.FOCUS_MODE_CONTINUOUS_VIDEO
            }

            params.setRecordingHint(true)
            mCamera!!.parameters = params
        }

        override fun onAutoFocus(success: Boolean, camera: Camera) {}

        override fun surfaceCreated(holder: SurfaceHolder) {
            // The Surface has been created, now tell the camera where to draw the preview.
            try {
                if (mCamera == null) return
                mCamera!!.setPreviewDisplay(holder)
                mCamera!!.startPreview()
            } catch (e: IOException) {
                Log.d("CameraCaptureActivity", "Error setting camera preview: " + e.message)
            }
        }

        override fun surfaceDestroyed(holder: SurfaceHolder) {
            // empty. Take care of releasing the Camera preview in your activity.
        }

        override fun surfaceChanged(holder: SurfaceHolder, format: Int, w: Int, h: Int) {
            // If your preview can change or rotate, take care of those events here.
            // Make sure to stop the preview before resizing or reformatting it.
            if (mHolder.surface == null) {
                // preview surface does not exist
                return
            }

            // stop preview before making changes
            try {
                mCamera!!.stopPreview()
            } catch (e: Exception) {
                // ignore: tried to stop a non-existent preview
            }

            // set preview size and make any resize, rotate or
            // reformatting changes here

            // start preview with new settings
            try {
                mCamera!!.setPreviewDisplay(mHolder)
                mCamera!!.setPreviewCallback(mPreviewCallback)
                mCamera!!.startPreview()
            } catch (e: Exception) {
                Log.d("CameraCaptureActivity", "Error starting camera preview: " + e.message)
            }
        }
    }
}
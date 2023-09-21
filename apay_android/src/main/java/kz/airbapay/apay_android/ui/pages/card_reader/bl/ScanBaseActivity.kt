package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.app.Activity
import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.Bitmap
import android.graphics.RectF
import android.hardware.Camera
import android.hardware.Camera.AutoFocusCallback
import android.hardware.Camera.PreviewCallback
import android.os.Bundle
import android.os.SystemClock
import android.util.Log
import android.view.OrientationEventListener
import android.view.Surface
import android.view.SurfaceHolder
import android.view.SurfaceView
import android.view.View
import android.view.ViewTreeObserver
import android.widget.FrameLayout
import java.io.IOException
import java.util.concurrent.Semaphore

/**
 * Any classes that subclass this must:
 *
 *
 * (1) set mIsPermissionCheckDone after the permission check is done, which should be sometime
 * before "onResume" is called
 *
 *
 * (2) Call setViewIds to set these resource IDs and initalize appropriate handlers
 */
internal abstract class ScanBaseActivity : Activity(), PreviewCallback, OnScanListener,
    OnCameraOpenListener {

    private var mCamera: Camera? = null
    private var mOrientationEventListener: OrientationEventListener? = null
    private val mMachineLearningSemaphore = Semaphore(1)
    private var mRotation = 0
    private var mSentResponse = false
    private var mIsActivityActive = false
    private var numberResults = HashMap<String, Int>()
    private var firstResultMs: Long = 0
    private var mTextureId = 0
    private var mRoiCenterYRatio = 0f
    private var mCameraThread: CameraThread? = null

    // set when this activity posts to the machineLearningThread
    private var mPredictionStartMs: Long = 0

    var mIsPermissionCheckDone = false
    private var errorCorrectionDurationMs: Long = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mOrientationEventListener = object : OrientationEventListener(this) {
            override fun onOrientationChanged(orientation: Int) {
                orientationChanged(orientation)
            }
        }
    }

    internal inner class MyGlobalListenerClass(
        private val cardRectangleId: Int,
        private val overlayId: Int
    ) : ViewTreeObserver.OnGlobalLayoutListener {

        override fun onGlobalLayout() {
            val xy = IntArray(2)
            val view = findViewById<View>(cardRectangleId)
            view.getLocationInWindow(xy)

            // convert from DP to pixels
            val radius = (11 * Resources.getSystem().displayMetrics.density).toInt()
            val rect = RectF(
                xy[0].toFloat(), xy[1].toFloat(),
                (xy[0] + view.width).toFloat(),
                (xy[1] + view.height).toFloat()
            )

            val overlay = findViewById<Overlay>(overlayId)
            overlay.setCircle(rect, radius)
            mRoiCenterYRatio = (xy[1] + view.height * 0.5f) / overlay.height
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            mIsPermissionCheckDone = true
        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Доступ к камере запрещен")
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onCameraOpen(camera: Camera?) {
        if (camera == null) {
            val intent = Intent()
            intent.putExtra(RESULT_CAMERA_OPEN_ERROR, true)
            setResult(RESULT_CANCELED, intent)
            finish()
        } else if (!mIsActivityActive) {
            camera.release()
        } else {
            mCamera = camera
            setCameraDisplayOrientation(
                this, Camera.CameraInfo.CAMERA_FACING_BACK,
                mCamera!!
            )
            // Create our Preview view and set it as the content of our activity.
            val cameraPreview = CameraPreview(this, this)
            val preview = findViewById<FrameLayout>(mTextureId)
            preview.addView(cameraPreview)
            mCamera!!.setPreviewCallback(this)
        }
    }

    private fun startCamera() {
        numberResults = HashMap()
        firstResultMs = 0

        if (mOrientationEventListener!!.canDetectOrientation()) {
            mOrientationEventListener!!.enable()
        }

        try {
            if (mIsPermissionCheckDone) {
                if (mCameraThread == null) {
                    mCameraThread = CameraThread()
                    mCameraThread!!.start()
                }
                mCameraThread!!.startCamera(this)
            }
        } catch (e: Exception) {
            val builder = AlertDialog.Builder(this)
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onPause() {
        super.onPause()
        if (mCamera != null) {
            mCamera!!.stopPreview()
            mCamera!!.setPreviewCallback(null)
            mCamera!!.release()
            mCamera = null
        }
        mOrientationEventListener!!.disable()
        mIsActivityActive = false
    }

    override fun onResume() {
        super.onResume()
        mIsActivityActive = true
        firstResultMs = 0
        numberResults = HashMap()
        mSentResponse = false
        startCamera()
    }

    fun setViewIds(cardRectangleId: Int, overlayId: Int, textureId: Int) {
        mTextureId = textureId
        findViewById<View>(cardRectangleId).viewTreeObserver
            .addOnGlobalLayoutListener(MyGlobalListenerClass(cardRectangleId, overlayId))
    }

    fun orientationChanged(orientation: Int) {
        var orientation = orientation
        if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN) return

        val info = Camera.CameraInfo()
        Camera.getCameraInfo(Camera.CameraInfo.CAMERA_FACING_BACK, info)
        orientation = (orientation + 45) / 90 * 90

        val rotation: Int = if (info.facing == Camera.CameraInfo.CAMERA_FACING_FRONT) {
            (info.orientation - orientation + 360) % 360
        } else {  // back-facing camera
            (info.orientation + orientation) % 360
        }

        if (mCamera != null) {
            try {
                val params = mCamera!!.parameters
                params.setRotation(rotation)
                mCamera!!.parameters = params
            } catch (e: Exception) {
                // This gets called often so we can just swallow it and wait for the next one
                e.printStackTrace()
            } catch (e: Error) {
                e.printStackTrace()
            }
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
        if (mMachineLearningSemaphore.tryAcquire()) {
            val mlThread = machineLearningThread
            val parameters = camera.parameters
            val width = parameters.previewSize.width
            val height = parameters.previewSize.height
            mPredictionStartMs = SystemClock.uptimeMillis()

            // Use the application context here because the machine learning thread's lifecycle
            // is connected to the application and not this activity
            mlThread!!.post(
                bytes, width, height, mRotation, this,
                this.applicationContext, mRoiCenterYRatio
            )
        }
    }

    override fun onBackPressed() {
        if (!mSentResponse && mIsActivityActive) {
            mSentResponse = true
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    fun incrementNumber(number: String) {
        var currentValue = numberResults[number]
        if (currentValue == null) {
            currentValue = 0
        }
        numberResults[number] = currentValue + 1
    }

    private val numberResult: String?
        get() {
            // Ugg there has to be a better way
            var result: String? = null
            var maxValue = 0
            for (number in numberResults.keys) {
                var value = 0
                val count = numberResults[number]
                if (count != null) {
                    value = count
                }
                if (value > maxValue) {
                    result = number
                    maxValue = value
                }
            }
            return result
        }

    protected abstract fun onCardScanned(numberResult: String?)

    override fun onFatalError() {
        val intent = Intent()
        intent.putExtra(RESULT_FATAL_ERROR, true)
        setResult(RESULT_CANCELED, intent)
        finish()
    }

    override fun onPrediction(
        number: String?, bitmap: Bitmap?,
        digitBoxes: List<DetectedBox?>?
    ) {
        if (!mSentResponse && mIsActivityActive) {
            if (number != null && firstResultMs == 0L) {
                firstResultMs = SystemClock.uptimeMillis()
            }
            number?.let { incrementNumber(it) }
            val duration = SystemClock.uptimeMillis() - firstResultMs
            if (firstResultMs != 0L && duration >= errorCorrectionDurationMs) {
                mSentResponse = true
                val numberResult = numberResult
                onCardScanned(numberResult)
            }
        }
        mMachineLearningSemaphore.release()
    }

    /**
     * A basic Camera preview class
     */
    inner class CameraPreview(context: Context?, private val mPreviewCallback: PreviewCallback) :
        SurfaceView(context), AutoFocusCallback, SurfaceHolder.Callback {
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

    companion object {
        const val RESULT_FATAL_ERROR = "result_fatal_error"
        const val RESULT_CAMERA_OPEN_ERROR = "result_camera_open_error"
        var machineLearningThread: MachineLearningThread? = null
            get() {
                if (field == null) {
                    field = MachineLearningThread()
                    Thread(field).start()
                }
                return field
            }
            private set
    }
}
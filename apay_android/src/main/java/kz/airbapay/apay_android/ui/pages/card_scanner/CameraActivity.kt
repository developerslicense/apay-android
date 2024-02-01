package kz.airbapay.apay_android.ui.pages.card_scanner

import android.graphics.Bitmap
import android.graphics.Matrix
import android.hardware.Camera
import android.hardware.Camera.PreviewCallback
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_scanner.CameraConnectionFragment.Companion.newInstance
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.allPermissionsGranted
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.chooseCamera
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.hasPermission
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.onImageAvailableImpl
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.onPreviewFrameImpl
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.onPreviewSizeChosen
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_activity_ext.requestPermission
import kz.airbapay.apay_android.ui.pages.card_scanner.rectangle_detector.Classifier
import kz.airbapay.apay_android.ui.pages.card_scanner.rectangle_detector.MultiBoxTracker
import kz.airbapay.apay_android.ui.pages.card_scanner.view.OverlayView

internal class CameraActivity : AppCompatActivity(),
    ImageReader.OnImageAvailableListener,
    PreviewCallback
{
    internal var previewWidth = 0
    internal var previewHeight = 0
    internal var handler: Handler? = null
    private var handlerThread: HandlerThread? = null
    internal var isProcessingFrame = false
    internal val yuvBytes = arrayOfNulls<ByteArray>(3)
    internal var rgbBytes: IntArray? = null
    internal var yRowStride = 0
    internal var postInferenceCallback: Runnable? = null
    internal var imageConverter: Runnable? = null
    internal var trackingOverlay: OverlayView? = null
    internal val detector: Classifier? = null
    internal var rgbFrameBitmap: Bitmap? = null
    internal var croppedBitmap: Bitmap? = null
    internal var cropCopyBitmap: Bitmap? = null
    internal var computingDetection = false
    internal var timestamp: Long = 0
    internal var frameToCropTransform: Matrix? = null
    internal var cropToFrameTransform: Matrix? = null
    internal var tracker: MultiBoxTracker? = null
    private val layoutId = R.layout.tfe_od_camera_connection_fragment_tracking

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(null)
        window.addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON)
        setContentView(R.layout.tfe_od_activity_camera)
        if (hasPermission()) {
            setFragment()
        } else {
            requestPermission()
        }
    }

    override fun onPreviewFrame(bytes: ByteArray, camera: Camera) {
        onPreviewFrameImpl(camera, bytes)
    }

    override fun onImageAvailable(reader: ImageReader) {
        onImageAvailableImpl(reader)
    }

    @Synchronized
    public override fun onResume() {
        super.onResume()
        handlerThread = HandlerThread("inference")
        handlerThread!!.start()
        handler = Handler(handlerThread!!.looper)
    }

    @Synchronized
    public override fun onPause() {
        handlerThread!!.quitSafely()
        try {
            handlerThread!!.join()
            handlerThread = null
            handler = null
        } catch (e: InterruptedException) {
            e.printStackTrace()
        }
        super.onPause()
    }

    override fun onRequestPermissionsResult(
        requestCode: Int, permissions: Array<String>, grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == PERMISSIONS_REQUEST) {
            if (allPermissionsGranted(grantResults)) {
                setFragment()
            } else {
                requestPermission()
            }
        }
    }

    private fun setFragment() {
        val cameraId = chooseCamera()
        val fragment: Fragment
        val camera2Fragment = newInstance(
            callback = { size: Size, rotation: Int ->
                previewHeight = size.height
                previewWidth = size.width
                onPreviewSizeChosen(size, rotation)
            },
            imageListener = this,
            layout = layoutId,
            inputSize = Size(640, 480)
        )
        camera2Fragment.setCamera(cameraId)
        fragment = camera2Fragment
        supportFragmentManager.beginTransaction().replace(R.id.container, fragment).commit()
    }
    
    companion object {
        internal const val PERMISSIONS_REQUEST = 1
    }
}

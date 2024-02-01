package kz.airbapay.apay_android.ui.pages.card_scanner

import android.app.Activity
import android.graphics.SurfaceTexture
import android.hardware.camera2.CameraCaptureSession
import android.hardware.camera2.CameraDevice
import android.hardware.camera2.CaptureRequest
import android.hardware.camera2.CaptureResult
import android.hardware.camera2.TotalCaptureResult
import android.media.ImageReader
import android.os.Bundle
import android.os.Handler
import android.os.HandlerThread
import android.util.Size
import android.util.SparseIntArray
import android.view.LayoutInflater
import android.view.Surface
import android.view.TextureView.SurfaceTextureListener
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext.closeCamera
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext.configureTransform
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext.createCameraPreviewSession
import kz.airbapay.apay_android.ui.pages.card_scanner.camera_fragment_ext.openCamera
import kz.airbapay.apay_android.ui.pages.card_scanner.view.AutoFitTextureView
import java.util.concurrent.Semaphore

internal class CameraConnectionFragment private constructor(
    val cameraConnectionCallback: (size: Size, rotation: Int) -> Unit,
    val imageListener: ImageReader.OnImageAvailableListener,
    private val layout: Int,
    val inputSize: Size,
    var cameraId: String? = null
) : Fragment() {
    val cameraOpenCloseLock = Semaphore(1)
    val captureCallback: CameraCaptureSession.CaptureCallback =
        object : CameraCaptureSession.CaptureCallback() {
            override fun onCaptureProgressed(
                session: CameraCaptureSession,
                request: CaptureRequest,
                partialResult: CaptureResult
            ) {
            }

            override fun onCaptureCompleted(
                session: CameraCaptureSession,
                request: CaptureRequest,
                result: TotalCaptureResult
            ) {
            }
        }
    var textureView: AutoFitTextureView? = null
    var captureSession: CameraCaptureSession? = null
    var cameraDevice: CameraDevice? = null
    var sensorOrientation: Int? = null
    var previewSize: Size? = null
    private var backgroundThread: HandlerThread? = null
    var backgroundHandler: Handler? = null
    var previewReader: ImageReader? = null
    var previewRequestBuilder: CaptureRequest.Builder? = null
    var previewRequest: CaptureRequest? = null

    val stateCallback: CameraDevice.StateCallback = object : CameraDevice.StateCallback() {
        override fun onOpened(cd: CameraDevice) {
            // This method is called when the camera is opened.  We start camera preview here.
            cameraOpenCloseLock.release()
            cameraDevice = cd
            createCameraPreviewSession()
        }

        override fun onDisconnected(cd: CameraDevice) {
            cameraOpenCloseLock.release()
            cd.close()
            cameraDevice = null
        }

        override fun onError(cd: CameraDevice, error: Int) {
            cameraOpenCloseLock.release()
            cd.close()
            cameraDevice = null
            val activity: Activity? = activity
            activity?.finish()
        }
    }

    private val surfaceTextureListener: SurfaceTextureListener = object : SurfaceTextureListener {
        override fun onSurfaceTextureAvailable(texture: SurfaceTexture, width: Int, height: Int) {
            openCamera(width, height)
        }

        override fun onSurfaceTextureSizeChanged(texture: SurfaceTexture, width: Int, height: Int) {
            configureTransform(width, height)
        }

        override fun onSurfaceTextureDestroyed(texture: SurfaceTexture) = true

        override fun onSurfaceTextureUpdated(texture: SurfaceTexture) {}
    }

    fun showToast() {
        val activity: Activity? = activity
        activity?.runOnUiThread { Toast.makeText(activity, "Failed", Toast.LENGTH_SHORT).show() }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(layout, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        textureView = view.findViewById<View>(R.id.texture) as AutoFitTextureView
    }

    override fun onResume() {
        super.onResume()
        startBackgroundThread()
        if (textureView!!.isAvailable) {
            openCamera(textureView!!.width, textureView!!.height)
        } else {
            textureView!!.surfaceTextureListener = surfaceTextureListener
        }
    }

    override fun onPause() {
        closeCamera()
        stopBackgroundThread()
        super.onPause()
    }

    /**
     * Starts a background thread and its [Handler].
     */
    private fun startBackgroundThread() {
        backgroundThread = HandlerThread("ImageListener")
        backgroundThread!!.start()
        backgroundHandler = Handler(backgroundThread!!.looper)
    }

    /**
     * Stops the background thread and its [Handler].
     */
    private fun stopBackgroundThread() {
        backgroundThread!!.quitSafely()
        try {
            backgroundThread!!.join()
            backgroundThread = null
            backgroundHandler = null
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        private val ORIENTATIONS = SparseIntArray()

        init {
            ORIENTATIONS.append(Surface.ROTATION_0, 90)
            ORIENTATIONS.append(Surface.ROTATION_90, 0)
            ORIENTATIONS.append(Surface.ROTATION_180, 270)
            ORIENTATIONS.append(Surface.ROTATION_270, 180)
        }

        fun newInstance(
            callback: (size: Size, rotation: Int) -> Unit,
            imageListener: ImageReader.OnImageAvailableListener,
            layout: Int,
            inputSize: Size,
            cameraId: String?
        ): CameraConnectionFragment {
            return CameraConnectionFragment(callback, imageListener, layout, inputSize, cameraId)
        }
    }
}

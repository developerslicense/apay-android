package kz.airbapay.apay_android.ui.pages.card_reader

import android.Manifest
import android.app.Activity
import android.app.AlertDialog
import android.content.Intent
import android.content.pm.PackageManager
import android.content.res.Resources
import android.graphics.RectF
import android.os.Build
import android.os.Bundle
import android.view.View
import android.view.ViewTreeObserver
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_reader.bl.ExecutorCamera
import kz.airbapay.apay_android.ui.pages.card_reader.bl.ExecutorML
import kz.airbapay.apay_android.ui.pages.card_reader.bl.MachineLearningThread
import kz.airbapay.apay_android.ui.pages.card_reader.bl.Overlay
import java.util.concurrent.Semaphore

internal class ScanActivity : Activity() {

    var executorML: ExecutorML? = null
    private var executorCamera: ExecutorCamera? = null
    private val mMachineLearningSemaphore = Semaphore(1)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.irdcs_activity_scan_card)

        findViewById<View>(R.id.cardRectangle).viewTreeObserver
            .addOnGlobalLayoutListener(MyGlobalListenerClass(R.id.cardRectangle, R.id.shadedBackground))

        executorML = ExecutorML(
            activity = this,
            releaseSemaphore = { mMachineLearningSemaphore.release() }
        )

        executorCamera = ExecutorCamera(
            activity = this,
            tryAcquire = { mMachineLearningSemaphore.tryAcquire() }
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 110)
            } else {
                executorCamera?.mIsPermissionCheckDone = true
            }
        } else {
            executorCamera?.mIsPermissionCheckDone = true
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
            executorCamera?.mRoiCenterYRatio = (xy[1] + view.height * 0.5f) / overlay.height
        }
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<String>,
        grantResults: IntArray
    ) {
        if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
            executorCamera?.mIsPermissionCheckDone = true

        } else {
            val builder = AlertDialog.Builder(this)
            builder.setMessage("Доступ к камере запрещен")
            val dialog = builder.create()
            dialog.show()
        }
    }

    override fun onPause() {
        super.onPause()
        executorCamera?.onPause()
        executorML?.mIsActivityActive = false
    }

    override fun onResume() {
        super.onResume()
        executorML?.onResume()
        executorCamera?.startCamera()
    }

    override fun onBackPressed() {
        if (!executorML!!.mSentResponse && executorML?.mIsActivityActive == true) {
            executorML?.mSentResponse = true
            val intent = Intent()
            setResult(RESULT_CANCELED, intent)
            finish()
        }
    }

    companion object {
        const val RESULT_CARD_NUMBER = "cardNumber"
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
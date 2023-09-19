package kz.airbapay.apay_android.ui.pages.card_reader

import android.app.Activity
import android.graphics.Bitmap
import android.view.ViewGroup.LayoutParams.MATCH_PARENT
import android.widget.LinearLayout
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageProxy
import androidx.camera.view.LifecycleCameraController
import androidx.camera.view.PreviewView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import androidx.lifecycle.LifecycleOwner
import com.google.accompanist.permissions.isGranted
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kz.airbapay.apay_android.ui.pages.card_reader.bl.ScanActivity
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun CardScannerPage(

) {
    val cameraPermissionState = rememberPermissionState(android.Manifest.permission.CAMERA)

    val activity = LocalContext.current as Activity
    val lifecycleOwner: LifecycleOwner = LocalLifecycleOwner.current
    val cameraController: LifecycleCameraController = remember {
        LifecycleCameraController(activity)
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        if (cameraPermissionState.status.isGranted) {
            Button(onClick = {
                val mainExecutor = ContextCompat.getMainExecutor(activity)
                cameraController.takePicture(
                    mainExecutor,
                    object : ImageCapture.OnImageCapturedCallback() {
                        override fun onCaptureSuccess(image: ImageProxy) {
                            println("aaaaqqqq+++++")
                            getCardDetails(
                                activity = activity,
                                bitmap = image.toBitmap()
                            )
                        }
                    })

            }, content = { Text("CLICK") })
            AndroidView(
                modifier = Modifier.fillMaxSize(),
                factory = {
                    PreviewView(activity)
                        .apply {
                            this.controller = cameraController
                            cameraController.bindToLifecycle(lifecycleOwner)

                            setBackgroundColor(ColorsSdk.bgBlock.toArgb())
                            layoutParams = LinearLayout.LayoutParams(MATCH_PARENT, MATCH_PARENT)
                            scaleType = PreviewView.ScaleType.FILL_START
                            implementationMode = PreviewView.ImplementationMode.COMPATIBLE
                        }
                },
                onRelease = {
                    cameraController.unbind()
                }
            )

        } else {
            Text("ERROR") //todo состояние ошибки
        }
    }

  /*  LaunchedEffect("onStart") {
        ScanActivity.warmUp(activity)
    }
*/
    LaunchedEffect("Permission") {
        if (!cameraPermissionState.status.isGranted && !cameraPermissionState.status.shouldShowRationale) {
            cameraPermissionState.launchPermissionRequest()
        }
    }
}


private fun getCardDetails(
    activity: Activity,
    bitmap: Bitmap
) {
    ScanActivity.start(activity)
}

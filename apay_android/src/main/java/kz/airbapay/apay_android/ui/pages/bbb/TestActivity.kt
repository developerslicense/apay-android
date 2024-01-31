package kz.airbapay.apay_android.ui.pages.bbb

//import com.google.mlkit.common.model.LocalModel
//import com.google.mlkit.vision.common.InputImage
//import com.google.mlkit.vision.objects.ObjectDetection
//import com.google.mlkit.vision.objects.ObjectDetector
//import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions

//import com.google.mlkit.common.model.LocalModel
//import com.google.mlkit.vision.objects.ObjectDetection
//import com.google.mlkit.vision.objects.ObjectDetection
//import com.google.mlkit.vision.objects.ObjectDetector
//import com.google.mlkit.vision.objects.custom.CustomObjectDetectorOptions
import android.annotation.SuppressLint
import android.graphics.Point
import android.os.Bundle
import android.util.Size
import android.widget.RelativeLayout
import androidx.annotation.OptIn
import androidx.appcompat.app.AppCompatActivity
import androidx.camera.core.CameraSelector
import androidx.camera.core.ExperimentalGetImage
import androidx.camera.core.ImageAnalysis
import androidx.camera.core.Preview
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.core.content.ContextCompat
import androidx.core.graphics.toRect
import androidx.lifecycle.LifecycleOwner
import com.google.common.util.concurrent.ListenableFuture
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import org.tensorflow.lite.support.image.TensorImage
import org.tensorflow.lite.task.vision.detector.ObjectDetector

internal class TestActivity : AppCompatActivity() {

    private lateinit var objectDetector: ObjectDetector
    private lateinit var cameraProviderFuture: ListenableFuture<ProcessCameraProvider>

    @OptIn(ExperimentalGetImage::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_test)
        cameraProviderFuture = ProcessCameraProvider.getInstance(this)

        cameraProviderFuture.addListener({
            val cameraProvider = cameraProviderFuture.get()
            bindPreview(cameraProvider) // Call the function here
        }, ContextCompat.getMainExecutor(this))

       /* val localModel = LocalModel.Builder()
            .setAssetFilePath("object_labeler2.tflite")
            .build()

        val customObjectDetectorOptions =
            CustomObjectDetectorOptions.Builder(localModel)
                .setDetectorMode(CustomObjectDetectorOptions.STREAM_MODE)
                .enableClassification()
                .setClassificationConfidenceThreshold(0.5f)
                .setMaxPerObjectLabelCount(4)
                .build()

        objectDetector = ObjectDetection.getClient(customObjectDetectorOptions)*/
    }

    @OptIn(ExperimentalGetImage::class)
    @SuppressLint("UnsafeExperimentalUsageError")
    private fun bindPreview(
        cameraProvider: ProcessCameraProvider
    ) {
        val preview: Preview = Preview.Builder().build()
        preview.setSurfaceProvider(findViewById<PreviewView>(R.id.previewView).surfaceProvider)

        val cameraSelector: CameraSelector = CameraSelector.Builder()
            .requireLensFacing(CameraSelector.LENS_FACING_BACK)
            .build()

        val point = Point()
        val imageAnalysis = ImageAnalysis.Builder()
            .setTargetResolution(Size(point.x, point.y))
            .setBackpressureStrategy(ImageAnalysis.STRATEGY_KEEP_ONLY_LATEST)
            .build()

        CoroutineScope(IO).launch {
            imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this@TestActivity)) { imageProxy ->
                val rotationDegrees = imageProxy.imageInfo.rotationDegrees
                val image = imageProxy.toBitmap()

                if (image != null) {
                    val image = TensorImage.fromBitmap(image)
                    val options = ObjectDetector.ObjectDetectorOptions.builder()
                        .setMaxResults(5)
                        .setScoreThreshold(0.5f)
                        .build()
                    val detector = ObjectDetector.createFromFileAndOptions(
                        this@TestActivity, // the application context
                        "object_labeler4.tflite", // must be same as the filename in assets folder
                        options
                    )

                    val result = detector.detect(image)
                    for (it in result) {
                        if (findViewById<RelativeLayout>(R.id.layout).childCount > 1) {
                            findViewById<RelativeLayout>(R.id.layout).removeViewAt(1)
                        }
                        val element = Draw(
                            this@TestActivity,
                            it.boundingBox.toRect(),
                            it.categories.firstOrNull()?.label ?: "Undefined"
//                        it.labels.firstOrNull()?.text ?: "Undefined"
                        )
                        findViewById<RelativeLayout>(R.id.layout).addView(element, 1)

                    }
                    imageProxy.close()
                }
            }
        }
        /*

        imageAnalysis.setAnalyzer(ContextCompat.getMainExecutor(this)) { imageProxy ->
            val rotationDegrees = imageProxy.imageInfo.rotationDegrees
            val image = imageProxy.image

            if (image != null) {
                val inputImage = InputImage.fromMediaImage(image, rotationDegrees)

                objectDetector
                    .process(inputImage)
                    .addOnFailureListener {
                        imageProxy.close()
                    }.addOnSuccessListener { objects ->
                        for (it in objects) {
                            if (findViewById<RelativeLayout>(R.id.layout).childCount > 1) {
                                findViewById<RelativeLayout>(R.id.layout).removeViewAt(1)
                            }
                            val element = Draw(
                                this,
                                it.boundingBox,
                                it.labels.firstOrNull()?.text ?: "Undefined"
                            )
                            findViewById<RelativeLayout>(R.id.layout).addView(element, 1)

                        }
                        imageProxy.close()
                    }
            }
        }*/

        cameraProvider.bindToLifecycle(
            this as LifecycleOwner,
            cameraSelector,
            imageAnalysis,
            preview
        )

    }
}
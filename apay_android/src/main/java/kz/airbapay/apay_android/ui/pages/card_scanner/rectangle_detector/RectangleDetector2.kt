package kz.airbapay.apay_android.ui.pages.card_scanner.rectangle_detector

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions

internal class RectangleDetector2 {
    private var objectDetector: ObjectDetector

    init {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification()
            .build()

        objectDetector = ObjectDetection.getClient(options)
    }

    fun doObjectDetection(
        detectedImage: Bitmap
    ) {

        val mutable = detectedImage.copy(Bitmap.Config.ARGB_8888, true)
        val canvas = Canvas(mutable)

        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        paint.strokeWidth = 6f

        val image = InputImage.fromBitmap(detectedImage, 0)

        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects: List<DetectedObject> ->
                for (detectedObject in detectedObjects) {
                    val boundingBox = detectedObject.boundingBox
                    canvas.drawRect(boundingBox, paint)
                }
            }
            .addOnFailureListener { e: Exception -> e.printStackTrace() }
    }
}

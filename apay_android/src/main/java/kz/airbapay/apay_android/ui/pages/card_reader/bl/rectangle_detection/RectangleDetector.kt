package kz.airbapay.apay_android.ui.pages.card_reader.bl.rectangle_detection

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import com.google.mlkit.vision.common.InputImage
import com.google.mlkit.vision.objects.DetectedObject
import com.google.mlkit.vision.objects.ObjectDetection
import com.google.mlkit.vision.objects.ObjectDetector
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions
import com.google.mlkit.vision.objects.defaults.PredefinedCategory
import kz.airbapay.apay_android.ui.pages.card_reader.bl.camera.OnRectangleListener

class RectangleDetector(listener: OnRectangleListener) {
    var objectDetector: ObjectDetector
    var listener: OnRectangleListener

    init {
        val options = ObjectDetectorOptions.Builder()
            .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
            .enableMultipleObjects()
            .enableClassification() // ◄◄ OPTIONAL
            .build()


        // (STEP 1-2 - "OBJECT DETECTION") "INSTANTIATION" OF "OBJECT DETECTOR":
        objectDetector = ObjectDetection.getClient(options)
        this.listener = listener
    }

    fun doObjectDetection(
        detectedImage: Bitmap,
        mlAction: () -> Unit
    ) {
//        Bitmap inputImage = uriToBitmap(image_uri);
//        Bitmap rotated = rotateBitmap(inputImage);


        // (STEP 4-2.1 → "OBJEC T DETECTION") CREATING "MUTABLE COBY" OF "ROTATED" BITMAP:
        val mutable = detectedImage.copy(Bitmap.Config.ARGB_8888, true)

        // (STEP 4-2.2 → "OBJECT DETECTION") CREATING "CANVAS" OBJECT:
        val canvas = Canvas(mutable)

        // (STEP 4-2.3 → "OBJECT DETECTION") CREATING A "PAINT" OBJECT:
        val paint = Paint()

        // (STEP 4-2.4 → "OBJECT DETECTION") "SETTING" THE "RECTANGLE COLOR":
        paint.color = Color.RED

        // (STEP 4-2.5 → "OBJECT DETECTION") "SETTING" THE "RECTANGLE STYLE":
        paint.style = Paint.Style.STROKE

        // (STEP 4-2.6 → "OBJECT DETECTION") "SETTING" THE "STROKE WIDTH":
        paint.strokeWidth = 3f


        // ▼ "DRAWING" THE "TEXT" ▼
        // (STEP 5-1.1 → "OBJECT DETECTION") CREATING A "PAINT" OBJECT FOR THE "TEXT":
        val paint2 = Paint()

        // (STEP 5-1.2 → "OBJECT DETECTION") "SETTING" THE "TEXT COLOR":
        paint2.color = Color.YELLOW

        // (STEP 5-1.3 → "OBJECT DETECTION") "SETTING" THE "TEXT SIZE":
        paint2.textSize = 18f


//        innerImage.setImageBitmap(rotated);


        // (STEP 2 - "OBJECT DETECTION") "PERFORMING" "INPUT IMAGE"
        //      → USING "BITMAP" AS "INPUT IMAGE":
        val image = InputImage.fromBitmap(detectedImage, 0)


        // (STEP 3 - "OBJECT DETECTION") "PROCESS" THE "IMAGE":
        objectDetector.process(image)
            .addOnSuccessListener { detectedObjects: List<DetectedObject> ->
                println("rrrrrrrrrr")
                // (STEP 4-1 → "OBJECT DETECTION") "GETTING INFORMATION" ABOUT "DETECTED OBJECTS":
                //      → WHICH CONTAIN "ONE ITEM"
                //      → IF "MULTIPLE OBJECT DETECTION" WASN'T "ENABLED":
                // ▼ LOOPING FOR "RECTANGLE" ▼
                for (detectedObject in detectedObjects) {
                    val boundingBox = detectedObject.boundingBox


                    // (STEP 4-3 → "OBJECT DETECTION") "DRAWING" A "RECTANGLE" ON "CANVAS":
                    canvas.drawRect(boundingBox, paint)


                    // ▼ LOOPING FOR "LABEL" ▼
                    for (label in detectedObject.labels) {
                        val text = label.text

                        // (STEP 5-2 → "OBJECT DETECTION") "DRAWING" A "TEXT" ON "CANVAS":
                        canvas.drawText(
                            text,
                            boundingBox.left.toFloat(),
                            boundingBox.top.toFloat(),
                            paint2
                        )
                        if (PredefinedCategory.FOOD == text) {
                        }
                        val index = label.index
                        if (PredefinedCategory.FOOD_INDEX == index) {
// todo проблемы:
//  1) вызывается один раз
//  2) выделяется все, что не поподя.
//  3) нужно как-то прямоугольник вписать в превьюшку, а не вырезанное изображение
                        }
                        val confidence = label.confidence
                        break
                    }
                }


                // (STEP 4-4 → "OBJECT DETECTION") SHOWING "MUTABLE" IN THE "INNER IMAGE":
//                                innerImage.setImageBitmap(mutable);
                listener.onRectangleFound(mutable)

//                mlAction()  //todo !!!
            }
            .addOnFailureListener { e: Exception? -> println("eeeeeeeee") }
    }
}

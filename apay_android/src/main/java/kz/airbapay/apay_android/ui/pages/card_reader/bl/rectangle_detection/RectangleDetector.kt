package kz.airbapay.apay_android.ui.pages.card_reader.bl.rectangle_detection;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;

import com.google.mlkit.vision.common.InputImage;
import com.google.mlkit.vision.objects.DetectedObject;
import com.google.mlkit.vision.objects.ObjectDetection;
import com.google.mlkit.vision.objects.ObjectDetector;
import com.google.mlkit.vision.objects.defaults.ObjectDetectorOptions;
import com.google.mlkit.vision.objects.defaults.PredefinedCategory;

import kz.airbapay.apay_android.ui.pages.card_reader.bl.camera.OnRectangleListener;

public class RectangleDetector {
    ObjectDetector objectDetector;
    OnRectangleListener listener;

    public RectangleDetector(OnRectangleListener listener) {
        ObjectDetectorOptions options =
                new ObjectDetectorOptions.Builder()
                        .setDetectorMode(ObjectDetectorOptions.SINGLE_IMAGE_MODE)
                        .enableMultipleObjects()
                        .enableClassification()  // ◄◄ OPTIONAL
                        .build();


        // (STEP 1-2 - "OBJECT DETECTION") "INSTANTIATION" OF "OBJECT DETECTOR":
        objectDetector = ObjectDetection.getClient(options);
        this.listener = listener;
    }

    public void doObjectDetection(Bitmap rotated) {
//        Bitmap inputImage = uriToBitmap(image_uri);
//        Bitmap rotated = rotateBitmap(inputImage);


        // (STEP 4-2.1 → "OBJEC T DETECTION") CREATING "MUTABLE COBY" OF "ROTATED" BITMAP:
        Bitmap mutable = rotated.copy(Bitmap.Config.ARGB_8888, true);

        // (STEP 4-2.2 → "OBJECT DETECTION") CREATING "CANVAS" OBJECT:
        Canvas canvas = new Canvas(mutable);

        // (STEP 4-2.3 → "OBJECT DETECTION") CREATING A "PAINT" OBJECT:
        Paint paint = new Paint();

        // (STEP 4-2.4 → "OBJECT DETECTION") "SETTING" THE "RECTANGLE COLOR":
        paint.setColor(Color.RED);

        // (STEP 4-2.5 → "OBJECT DETECTION") "SETTING" THE "RECTANGLE STYLE":
        paint.setStyle(Paint.Style.STROKE);

        // (STEP 4-2.6 → "OBJECT DETECTION") "SETTING" THE "STROKE WIDTH":
        paint.setStrokeWidth(3);


        // ▼ "DRAWING" THE "TEXT" ▼
        // (STEP 5-1.1 → "OBJECT DETECTION") CREATING A "PAINT" OBJECT FOR THE "TEXT":
        Paint paint2 = new Paint();

        // (STEP 5-1.2 → "OBJECT DETECTION") "SETTING" THE "TEXT COLOR":
        paint2.setColor(Color.YELLOW);

        // (STEP 5-1.3 → "OBJECT DETECTION") "SETTING" THE "TEXT SIZE":
        paint2.setTextSize(18);


//        innerImage.setImageBitmap(rotated);


        // (STEP 2 - "OBJECT DETECTION") "PERFORMING" "INPUT IMAGE"
        //      → USING "BITMAP" AS "INPUT IMAGE":
        InputImage image = InputImage.fromBitmap(rotated, 0);


        // (STEP 3 - "OBJECT DETECTION") "PROCESS" THE "IMAGE":
        objectDetector.process(image)
                .addOnSuccessListener(
                        detectedObjects -> {
                            System.out.println("rrrrrrrrrr");
                            // (STEP 4-1 → "OBJECT DETECTION") "GETTING INFORMATION" ABOUT "DETECTED OBJECTS":
                            //      → WHICH CONTAIN "ONE ITEM"
                            //      → IF "MULTIPLE OBJECT DETECTION" WASN'T "ENABLED":
                            // ▼ LOOPING FOR "RECTANGLE" ▼
                            for (DetectedObject detectedObject : detectedObjects) {
                                Rect boundingBox = detectedObject.getBoundingBox();


                                // (STEP 4-3 → "OBJECT DETECTION") "DRAWING" A "RECTANGLE" ON "CANVAS":
                                canvas.drawRect(boundingBox, paint);


                                // ▼ LOOPING FOR "LABEL" ▼
                                for (DetectedObject.Label label : detectedObject.getLabels()) {
                                    String text = label.getText();

                                    // (STEP 5-2 → "OBJECT DETECTION") "DRAWING" A "TEXT" ON "CANVAS":
                                    canvas.drawText(
                                            text,
                                            boundingBox.left,
                                            boundingBox.top,
                                            paint2
                                    );


                                    if (PredefinedCategory.FOOD.equals(text)) {

                                    }

                                    int index = label.getIndex();

                                    if (PredefinedCategory.FOOD_INDEX == index) {

                                    }

                                    float confidence = label.getConfidence();
                                    break;
                                }
                            }


                            // (STEP 4-4 → "OBJECT DETECTION") SHOWING "MUTABLE" IN THE "INNER IMAGE":
//                                innerImage.setImageBitmap(mutable);
                            listener.onRectangleFound(mutable);

                        })

                .addOnFailureListener(
                        e -> {
                            System.out.println("eeeeeeeee");

                            // Task failed with an exception
                            // ...
                        });
    }

}

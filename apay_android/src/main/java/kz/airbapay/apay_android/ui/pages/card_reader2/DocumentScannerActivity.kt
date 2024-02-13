package kz.airbapay.apay_android.ui.pages.card_reader2

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.os.Build
import android.os.Bundle
import android.widget.FrameLayout
import androidx.appcompat.app.AppCompatActivity
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_reader2.camera.ExecutorCamera2
import kz.airbapay.apay_android.ui.pages.card_reader2.extensions.move
import kz.airbapay.apay_android.ui.pages.card_reader2.models.Quad
import kz.airbapay.apay_android.ui.pages.card_reader2.ui.Rectangle
import org.opencv.core.Point

/**
 * This class contains the main document scanner code. It opens the camera, lets the user
 * take a photo of a document (homework paper, business card, etc.), detects document corners,
 * allows user to make adjustments to the detected corners, depending on options, and saves
 * the cropped document. It allows the user to do this for 1 or more documents.
 *
 * @constructor creates document scanner activity
 */
class DocumentScannerActivity : AppCompatActivity() {

    private val cropperOffsetWhenCornersNotFound = 100.0
    private var rectangle: FrameLayout? = null

    private val camera = ExecutorCamera2(
        activity = this,
        onGetPhoto = { bitmap ->

            val corners = try {
                val (topLeft, topRight, bottomLeft, bottomRight) = getDocumentCorners(bitmap)
                Quad(topLeft, topRight, bottomRight, bottomLeft)
            } catch (exception: Exception) {
                finishIntentWithError(
                    "unable to get document corners: ${exception.message}"
                )
                return@ExecutorCamera2
            }
            println("aaaaaaaaaaaaaaa")
            println(corners.bottomLeftCorner)
            println(corners.bottomRightCorner)
            println(corners.corners)
            println(corners.edges)
            rectangle?.removeAllViews()
            rectangle?.addView(Rectangle(this, corners))
            /*document = Document(originalPhotoPath, photo.width, photo.height, corners)
            document?.let { document ->
                documents.add(document)
                addSelectedCornersAndOriginalPhotoPathToDocuments()
            }

            // create cropped document image, and return file path to complete document scan
            cropDocumentAndFinishIntent()*/

        }
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        try {
            // load OpenCV
            System.loadLibrary("opencv_java4")
        } catch (exception: Exception) {
            finishIntentWithError(
                "error starting OpenCV: ${exception.message}"
            )
        }

        setContentView(R.layout.irdcs_activity_scan_card)
        rectangle = findViewById(R.id.textureRectangle)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 110)
            } else {
                camera.mIsPermissionCheckDone = true
            }
        } else {
            camera.mIsPermissionCheckDone = true
        }
    }

    override fun onResume() {
        super.onResume()
        camera.startCamera()
    }

    override fun onPause() {
        super.onPause()
        camera.onPause()
    }

    /**
     * Pass in a photo of a document, and get back 4 corner points (top left, top right, bottom
     * right, bottom left). This tries to detect document corners, but falls back to photo corners
     * with slight margin in case we can't detect document corners.
     *
     * @param photo the original photo with a rectangular document
     * @return a List of 4 OpenCV points (document corners)
     */
    private fun getDocumentCorners(photo: Bitmap): List<Point> {
        val cornerPoints: List<Point>? = DocumentDetector().findDocumentCorners(photo)

        // if cornerPoints is null then default the corners to the photo bounds with a margin
        return cornerPoints ?: listOf(
            Point(0.0, 0.0).move(
                cropperOffsetWhenCornersNotFound,
                cropperOffsetWhenCornersNotFound
            ),
            Point(photo.width.toDouble(), 0.0).move(
                -cropperOffsetWhenCornersNotFound,
                cropperOffsetWhenCornersNotFound
            ),
            Point(0.0, photo.height.toDouble()).move(
                cropperOffsetWhenCornersNotFound,
                -cropperOffsetWhenCornersNotFound
            ),
            Point(photo.width.toDouble(), photo.height.toDouble()).move(
                -cropperOffsetWhenCornersNotFound,
                -cropperOffsetWhenCornersNotFound
            )
        )
    }

  /*

    private fun addSelectedCornersAndOriginalPhotoPathToDocuments() {
        // only add document it's not null (the current document photo capture, and corner
        // detection are successful)
        document?.let { document ->
            // convert corners from image preview coordinates to original photo coordinates
            // (original image is probably bigger than the preview image)
            val cornersInOriginalImageCoordinates = imageView.corners
                .mapPreviewToOriginalImageCoordinates(
                    imageView.imagePreviewBounds,
                    imageView.imagePreviewBounds.height() / document.originalPhotoHeight
                )
            document.corners = cornersInOriginalImageCoordinates
            documents.add(document)
        }
    }

    private fun cropDocumentAndFinishIntent() {
        val croppedImageResults = arrayListOf<String>()
        for ((pageNumber, document) in documents.withIndex()) {
            // crop document photo by using corners
            val croppedImage: Bitmap = try {
                ImageUtil().crop(
                    document.originalPhotoFilePath,
                    document.corners
                )
            } catch (exception: Exception) {
                finishIntentWithError("unable to crop image: ${exception.message}")
                return
            }

            // delete original document photo
            File(document.originalPhotoFilePath).delete()

            // save cropped document photo
            try {
                val croppedImageFile = FileUtil().createImageFile(this, pageNumber)
                croppedImage.saveToFile(croppedImageFile, croppedImageQuality)
                croppedImageResults.add(Uri.fromFile(croppedImageFile).toString())
            } catch (exception: Exception) {
                finishIntentWithError(
                    "unable to save cropped image: ${exception.message}"
                )
            }
        }

        // return array of cropped document photo file paths
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra("croppedImageResults", croppedImageResults)
        )
        finish()
    }*/

    /**
     * This ends the document scanner activity, and returns an error message that can be
     * used to debug error
     *
     * @param errorMessage an error message
     */
    private fun finishIntentWithError(errorMessage: String) {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra("error", errorMessage)
        )
        finish()
    } //todo
}
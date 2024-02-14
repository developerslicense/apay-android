package kz.airbapay.apay_android.ui.pages.card_reader2.utils

import android.content.Context
import android.graphics.Bitmap
import android.graphics.Matrix

internal fun getBitmap(
    bytes: ByteArray,
    width: Int,
    height: Int,
    context: Context
): Bitmap {

    // try reading image without OpenCV
    var imageBitmap = YUV_toRGB.convert(bytes, width, height, context)
//    var imageBitmap = BitmapFactory.decodeByteArray(bytes)

    imageBitmap = Bitmap.createBitmap(
        imageBitmap,
        0,
        0,
        imageBitmap.width,
        imageBitmap.height,
        Matrix().apply { postRotate(0f) }, // todo 90 180 270
        true
    )
    return imageBitmap
//    Utils.bitmapToMat(imageBitmap, image)
//
//    val bitmap = Bitmap.createBitmap(image.cols(), image.rows(), Bitmap.Config.ARGB_8888)
//    Utils.matToBitmap(image, bitmap)
//
//    return bitmap
}

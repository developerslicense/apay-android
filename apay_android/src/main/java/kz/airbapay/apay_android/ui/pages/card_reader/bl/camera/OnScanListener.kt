package kz.airbapay.apay_android.ui.pages.card_reader.bl.camera

import android.graphics.Bitmap
import kz.airbapay.apay_android.ui.pages.card_reader.bl.card_number_detection.DetectedBox

internal interface OnScanListener {
    fun onPrediction(number: String?, bitmap: Bitmap?, digitBoxes: List<DetectedBox?>?)
    fun onFatalError()
}
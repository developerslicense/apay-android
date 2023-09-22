package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.graphics.Bitmap

internal interface OnScanListener {
    fun onPrediction(number: String?, bitmap: Bitmap?, digitBoxes: List<DetectedBox?>?)
    fun onFatalError()
}
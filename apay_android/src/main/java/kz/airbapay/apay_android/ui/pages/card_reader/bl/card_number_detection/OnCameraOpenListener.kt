package kz.airbapay.apay_android.ui.pages.card_reader.bl.card_number_detection

import android.hardware.Camera

internal interface OnCameraOpenListener {
    fun onCameraOpen(camera: Camera?)
}
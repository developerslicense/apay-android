package kz.airbapay.apay_android.ui.pages.card_reader.bl

import android.hardware.Camera

internal interface OnCameraOpenListener {
    fun onCameraOpen(camera: Camera?)
}
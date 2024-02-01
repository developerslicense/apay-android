package kz.airbapay.apay_android.ui.pages.card_scanner.camera

import android.util.Size

interface ConnectionCallback {
    fun onPreviewSizeChosen(size: Size?, cameraRotation: Int)
}
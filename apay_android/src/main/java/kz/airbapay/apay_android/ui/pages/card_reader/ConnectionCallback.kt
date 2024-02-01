package kz.airbapay.apay_android.ui.pages.card_reader

import android.util.Size

@Deprecated("")
interface ConnectionCallback {
    fun onPreviewSizeChosen(size: Size, cameraRotation: Int)
}
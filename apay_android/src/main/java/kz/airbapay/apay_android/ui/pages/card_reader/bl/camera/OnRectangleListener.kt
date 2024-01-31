package kz.airbapay.apay_android.ui.pages.card_reader.bl.camera

import android.graphics.Bitmap

interface OnRectangleListener {
    fun onRectangleFound(mutable: Bitmap)
}
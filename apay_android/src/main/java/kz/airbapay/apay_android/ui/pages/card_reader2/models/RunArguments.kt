package kz.airbapay.apay_android.ui.pages.card_reader2.models

import android.content.Context
import kz.airbapay.apay_android.ui.pages.card_reader.bl.OnScanListener

internal class RunArguments(
    val mFrameBytes: ByteArray?,
    val mWidth: Int?,
    val mHeight: Int?,
    val mSensorOrientation: Int?,
    val mScanListener: OnScanListener,
    val mContext: Context,
    val mRoiCenterYRatio: Float?
)
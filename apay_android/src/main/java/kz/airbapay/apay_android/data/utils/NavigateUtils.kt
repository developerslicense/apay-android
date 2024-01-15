package kz.airbapay.apay_android.data.utils

import android.app.Activity
import android.content.Intent
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ARG_CARD_DATE_EXPIRED
import kz.airbapay.apay_android.data.constant.ARG_CARD_ID
import kz.airbapay.apay_android.data.constant.ARG_CARD_PAN
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.ui.pages.acquiring.AcquiringActivity
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity
import kz.airbapay.apay_android.ui.pages.error.ErrorActivity
import kz.airbapay.apay_android.ui.pages.error.RepeatActivity
import kz.airbapay.apay_android.ui.pages.home.HomeActivity
import kz.airbapay.apay_android.ui.pages.startview.StartProcessingActivity
import kz.airbapay.apay_android.ui.pages.success.SuccessActivity

internal fun backToStartPage(activity: Activity) {
    val intent = Intent(activity, StartProcessingActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openHome( //todo ??? finish
    activity: Activity,
    cardId: String? = null,
    cardPan: String? = null,
    cardDateExpired: String? = null
) {
    val intent = Intent(activity, HomeActivity::class.java)
    intent.putExtra(ARG_CARD_ID, cardId)
    intent.putExtra(ARG_CARD_PAN, cardPan)
    intent.putExtra(ARG_CARD_DATE_EXPIRED, cardDateExpired)

    activity.startActivity(intent)
    activity.finish()
}

internal fun openRepeat(activity: Activity) {
    val intent = Intent(activity, RepeatActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openErrorPageWithCondition(
    errorCode: Int?,
    activity: Activity
) {
    val intent = Intent(activity, ErrorActivity::class.java)
    intent.putExtra(ARG_ERROR_CODE, errorCode)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openAcquiring(
    redirectUrl: String?,
    activity: Activity
) {
    val intent = Intent(activity, AcquiringActivity::class.java)
    intent.putExtra(ARG_ACTION, redirectUrl)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openSuccess(activity: Activity) {
    val intent = Intent(activity, SuccessActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openCardScanner(activity: HomeActivity) {
    // todo если возникнут проблемы с камерой, то используй https://github.com/android/camera-samples
    activity.scanResultLauncher?.launch(Intent(activity, ScanActivity::class.java))
}
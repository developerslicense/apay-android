package kz.airbapay.apay_android.data.utils

import android.app.Activity
import android.content.Intent
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.ui.pages.acquiring.AcquiringActivity
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity
import kz.airbapay.apay_android.ui.pages.error.ErrorActivity
import kz.airbapay.apay_android.ui.pages.error.RepeatActivity
import kz.airbapay.apay_android.ui.pages.googlepay.GooglePayActivity
import kz.airbapay.apay_android.ui.pages.home.HomeActivity
import kz.airbapay.apay_android.ui.pages.startview.StartProcessingActivity
import kz.airbapay.apay_android.ui.pages.success.SuccessActivity

internal fun backToStartPage(activity: Activity) {
    val intent = Intent(activity, StartProcessingActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openHome( //todo ??? finish
    activity: Activity
) {
    val intent = Intent(activity, HomeActivity::class.java)
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

internal fun openGooglePay(
    redirectUrl: String?,
    activity: Activity
) {
    val intent = Intent(activity, GooglePayActivity::class.java)
    intent.putExtra(ARG_ACTION, redirectUrl)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openSuccess(activity: Activity) {
    if (DataHolder.redirectToCustomSuccessPage != null) {
        DataHolder.redirectToCustomSuccessPage?.invoke()

    } else {
        val intent = Intent(activity, SuccessActivity::class.java)
        activity.startActivity(intent)
        activity.finishAffinity()
    }
}

internal fun openCardScanner(activity: HomeActivity) {
    // todo если возникнут проблемы с камерой, то используй https://github.com/android/camera-samples
    activity.scanResultLauncher?.launch(Intent(activity, ScanActivity::class.java))
}
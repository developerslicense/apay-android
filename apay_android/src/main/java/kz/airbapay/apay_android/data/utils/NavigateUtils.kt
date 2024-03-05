package kz.airbapay.apay_android.data.utils

import android.app.Activity
import android.content.Intent
import io.card.payment.CardIOActivity
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.data.constant.SCAN_REQUEST_CODE
import kz.airbapay.apay_android.ui.pages.acquiring.AcquiringActivity
import kz.airbapay.apay_android.ui.pages.error.ErrorActivity
import kz.airbapay.apay_android.ui.pages.error.RepeatActivity
import kz.airbapay.apay_android.ui.pages.googlepay.webview.GooglePayActivity
import kz.airbapay.apay_android.ui.pages.home.HomeActivity
import kz.airbapay.apay_android.ui.pages.startview.StartProcessingActivity
import kz.airbapay.apay_android.ui.pages.success.SuccessActivity

internal fun Activity.backToApp(
    isSuccess: Boolean = false
) {
    DataHolder.frontendCallback?.invoke(this, isSuccess)
    DataHolder.frontendCallback = null
}

internal fun backToStartPage(activity: Activity) {
    val intent = Intent(activity, StartProcessingActivity::class.java)
    activity.startActivity(intent)
    activity.finish()
}

internal fun openHome(
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
        DataHolder.redirectToCustomSuccessPage?.invoke(activity)

    } else {
        val intent = Intent(activity, SuccessActivity::class.java)
        activity.startActivity(intent)
        activity.finishAffinity()
    }
}

internal fun openCardScanner(activity: Activity) {
    // todo если возникнут проблемы с камерой, то используй https://github.com/android/camera-samples

    // todo оставил на всякий сдучай
//    activity.scanResultLauncher?.launch(Intent(activity, ScanActivity::class.java))

    val scanIntent = Intent(activity, CardIOActivity::class.java)

    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
    scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
    scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
    scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
    scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
    scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
    scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true)
    activity.startActivityForResult(
        scanIntent,
        SCAN_REQUEST_CODE,
        null
    )
}

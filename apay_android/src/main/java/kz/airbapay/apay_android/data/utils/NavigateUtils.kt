package kz.airbapay.apay_android.data.utils

import android.content.Intent
import androidx.core.net.toUri
import androidx.navigation.NavController
import kz.airbapay.apay_android.AirbaPayActivity
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.ROUTES_ERROR_FINAL
import kz.airbapay.apay_android.data.constant.ROUTES_ERROR_SOMETHING_WRONG
import kz.airbapay.apay_android.data.constant.ROUTES_HOME
import kz.airbapay.apay_android.data.constant.ROUTES_REPEAT
import kz.airbapay.apay_android.data.constant.ROUTES_SUCCESS
import kz.airbapay.apay_android.data.constant.TEMPLATE_DEEP_LINK_ACQUIRING
import kz.airbapay.apay_android.data.constant.TEMPLATE_DEEP_LINK_GOOGLE_PAY
import kz.airbapay.apay_android.data.constant.TEMPLATE_ROUTES_ERROR
import kz.airbapay.apay_android.data.constant.TEMPLATE_ROUTES_ERROR_WITH_INSTRUCTION
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity

internal fun backToHome(navController: NavController) {
    navController.popBackStack(
        route = ROUTES_HOME,
        inclusive = false
    )
}

internal fun openRepeat(navController: NavController) {
    navController.navigate(route = ROUTES_REPEAT)
}

internal fun openErrorPageWithCondition(
    errorCode: Int?,
    navController: NavController
) {
    val error = initErrorsCodeByCode(errorCode ?: 1)

    if (error == ErrorsCode.error_1) {
        navController.navigate(ROUTES_ERROR_SOMETHING_WRONG)

    } else if (error.code == ErrorsCode.error_5020.code || errorCode == null) {
        navController.navigate(ROUTES_ERROR_FINAL)

    } else if (error.code== ErrorsCode.error_5999.code && DataHolder.bankCode?.isNotBlank() == true) {
        val deepLink = TEMPLATE_ROUTES_ERROR_WITH_INSTRUCTION +
                "?${ARG_ERROR_CODE}=${error.code}".toUri()
        navController.navigate(deepLink)

    } else {
        val deepLink = TEMPLATE_ROUTES_ERROR +
                "?${ARG_ERROR_CODE}=${error.code}".toUri()

        navController.navigate(deepLink)
    }
}

internal fun openAcquiring(
    redirectUrl: String?,
    navController: NavController
) {
    val deepLink = TEMPLATE_DEEP_LINK_ACQUIRING +
            "?$ARG_ACTION=${redirectUrl}".toUri()

    navController.navigate(deepLink)
}

internal fun openGooglePay(
    redirectUrl: String?,
    navController: NavController
) {
    val deepLink = TEMPLATE_DEEP_LINK_GOOGLE_PAY +
            "?$ARG_ACTION=${redirectUrl}".toUri()

    navController.navigate(deepLink)
}

internal fun openSuccess(navController: NavController?) {
    navController?.navigate(ROUTES_SUCCESS)
}

internal fun openCardScanner(activity: AirbaPayActivity) {
 // todo если возникнут проблемы с камерой, то используй https://github.com/android/camera-samples
    activity.scanResultLauncher?.launch(Intent(activity, ScanActivity::class.java))
}
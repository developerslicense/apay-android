package kz.airbapay.apay_android.data.utils

import androidx.core.net.toUri
import androidx.navigation.NavController
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ARG_IS_RETRY
import kz.airbapay.apay_android.data.constant.TEMPLATE_DEEP_LINK_WEB_VIEW
import kz.airbapay.apay_android.data.model.Secure3D

internal fun openWebView(
    secure3D: Secure3D?,
    isRetry: Boolean,
    navController: NavController
) {
    val deepLink = TEMPLATE_DEEP_LINK_WEB_VIEW +
            "?$ARG_IS_RETRY=$isRetry" +
            "?$ARG_ACTION=${secure3D?.action}".toUri()

    navController.navigate(deepLink)
}

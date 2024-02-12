package kz.airbapay.apay_android.ui.pages.googlepay

import android.app.Activity
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GPayNative
import kz.airbapay.apay_android.ui.pages.googlepay.webview.GPayWebView

@Composable
internal fun GPayView(
    openGooglePayForWebFlow: () -> Unit,
//    clickOnNativeFlow: () -> Unit,
    isNative: Boolean = true
) {
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    Spacer(modifier = Modifier.height(16.dp))

    if (isNative
        && !DataHolder.gatewayMerchantId.isNullOrBlank()
        && !DataHolder.gateway.isNullOrBlank()
    ) {
        GPayNative(
            onClick = {

                println("click native aaaaaaaaaaaaaaa 1")/*clickOnNativeFlow()*/
                /*initAuth(
                    activity = activity,
                    coroutineScope = coroutineScope,
                    onSuccess = {
                        println("click native aaaaaaaaaaaaaaa_1")
                    },
                    onFailed = {},
                    onNotSecurity = {}
                )*/
            }
        )

    } else {
        GPayWebView(openGooglePayForWebFlow)
    }
}

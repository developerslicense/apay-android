package kz.airbapay.apay_android.ui.pages.googlepay

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.BaseGooglePayActivity
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GPayNative
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.PaymentUiState
import kz.airbapay.apay_android.ui.pages.googlepay.webview.GPayWebView
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
internal fun GPayView(
    openGooglePayForWebFlow: () -> Unit,
) {
    if (!DataHolder.hideInternalGooglePayButton) {
        val activity = LocalContext.current as BaseGooglePayActivity
        val coroutineScope = rememberCoroutineScope()

        Spacer(modifier = Modifier.height(16.dp))

        if (DataHolder.isGooglePayNative
            && !DataHolder.gatewayMerchantId.isNullOrBlank()
            && !DataHolder.gateway.isNullOrBlank()
        ) {
            val hasGooglePay = activity.paymentModel?.paymentUiState?.collectAsState()

            GPayNative(
                isEnabled = hasGooglePay?.value == PaymentUiState.Available,
                onClick = {

                    initAuth(
                        activity = activity,
                        coroutineScope = coroutineScope,
                        onSuccess = { onResult(activity) },
                        onFailed = {},
                        onNotSecurity = { onResult(activity) }
                    )
                }
            )

        } else {
            GPayWebView(openGooglePayForWebFlow)
        }
    }
}

private fun onResult(activity: BaseGooglePayActivity) {
    val task = activity.paymentModel?.getLoadPaymentDataTask(DataHolder.purchaseAmount)
    task?.addOnCompleteListener(activity.paymentDataLauncher::launch)
}

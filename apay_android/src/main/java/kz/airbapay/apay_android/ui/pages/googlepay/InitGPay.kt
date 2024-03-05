package kz.airbapay.apay_android.ui.pages.googlepay

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayGooglePayNativeView
import kz.airbapay.apay_android.ui.pages.googlepay.webview.GooglePayWebView

@Composable
internal fun GPayView(
    airbaPayBaseGooglePay: AirbaPayBaseGooglePay,
    openGooglePayForWebFlow: () -> Unit,
) {
    if (!DataHolder.hideInternalGooglePayButton) {

        Spacer(modifier = Modifier.height(16.dp))

        if (DataHolder.isGooglePayNative
            && !DataHolder.gatewayMerchantId.isNullOrBlank()
            && !DataHolder.gateway.isNullOrBlank()
        ) {

            AirbaPayGooglePayNativeView(
                airbaPayBaseGooglePay = airbaPayBaseGooglePay,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            )

        } else {
            GooglePayWebView(openGooglePayForWebFlow)
        }
    }
}

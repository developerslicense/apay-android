package kz.airbapay.apay_android.ui.pages.googlepay

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GooglePayUtil
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.PaymentUiState
import kz.airbapay.apay_android.ui.pages.googlepay.webview.GooglePayWebView
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
internal fun GPayView(
    airbaPayBaseGooglePay: AirbaPayBaseGooglePay,
    openGooglePayForWebFlow: () -> Unit,
) {
    val keyguardManager = (LocalContext.current as Activity).getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    if (DataHolder.renderInStandardFlowGooglePay) {

        Spacer(modifier = Modifier.height(16.dp))

        if (DataHolder.isGooglePayNative
            && !DataHolder.gatewayMerchantId.isNullOrBlank()
            && !DataHolder.gateway.isNullOrBlank()
        ) {
            val hasGooglePay = airbaPayBaseGooglePay.paymentModel?.paymentUiState?.collectAsState()
            val coroutineScope = rememberCoroutineScope()
            val activity = LocalContext.current as Activity


            AndroidView(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                factory = { context ->
                    PayButton(context).apply {
                        this.initialize(
                            ButtonOptions.newBuilder()
                                .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                                .setButtonType(ButtonConstants.ButtonType.PLAIN)
                                .setCornerRadius(8)
                                .setAllowedPaymentMethods(GooglePayUtil.allowedPaymentMethods().toString())
                                .build()
                        )
                    }
                },
                update = { button ->
                    button.apply {

                        this.isEnabled = hasGooglePay?.value == PaymentUiState.Available

                        setOnClickListener {
                            if (keyguardManager.isKeyguardSecure) {
                                initAuth(
                                    activity = activity,
                                    coroutineScope = coroutineScope,
                                    onSuccess = { airbaPayBaseGooglePay.onResultGooglePay() },
                                    onNotSecurity = { airbaPayBaseGooglePay.onResultGooglePay() }
                                )

                            } else {
                                airbaPayBaseGooglePay.onResultGooglePay()
                            }
                        }
                    }
                }
            )

        } else {
            GooglePayWebView(openGooglePayForWebFlow)
        }
    }
}

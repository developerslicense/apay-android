package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.app.Activity
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
fun AirbaPayGooglePayNativeView(
    airbaPayBaseGooglePay: AirbaPayBaseGooglePay,
    modifier: Modifier = Modifier,
    buttonTheme: Int = ButtonConstants.ButtonTheme.DARK,
    buttonType: Int = ButtonConstants.ButtonType.PLAIN,
    cornerRadius: Int = 8
) {
    val activity = LocalContext.current as Activity
    val hasGooglePay = airbaPayBaseGooglePay.paymentModel?.paymentUiState?.collectAsState()
    val coroutineScope = rememberCoroutineScope()

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PayButton(context).apply {
                this.initialize(
                    ButtonOptions.newBuilder()
                        .setButtonTheme(buttonTheme)
                        .setButtonType(buttonType)
                        .setCornerRadius(cornerRadius)
                        .setAllowedPaymentMethods(GooglePayUtil.allowedPaymentMethods().toString())
                        .build()
                )
            }
        },
        update = { button ->
            button.apply {

                this.isEnabled = hasGooglePay?.value == PaymentUiState.Available

                setOnClickListener {
                    initAuth(
                        activity = activity,
                        coroutineScope = coroutineScope,
                        onSuccess = { airbaPayBaseGooglePay.onResultGooglePay() },
                        onFailed = {},
                        onNotSecurity = { airbaPayBaseGooglePay.onResultGooglePay() }
                    )
                }
            }
        }
    )
}
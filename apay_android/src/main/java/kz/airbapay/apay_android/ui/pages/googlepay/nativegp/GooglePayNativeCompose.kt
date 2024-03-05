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
fun GooglePayNativeCompose(
    airbaPayBaseGooglePay: AirbaPayBaseGooglePay,
    modifier: Modifier
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


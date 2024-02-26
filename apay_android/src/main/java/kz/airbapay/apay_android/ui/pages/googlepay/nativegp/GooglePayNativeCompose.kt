package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
fun GooglePayNativeCompose(
    modifier: Modifier
) {
    val activity = LocalContext.current as BaseComposeGooglePayActivity
    val hasGooglePay = activity.paymentModel?.paymentUiState?.collectAsState()
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
                        onSuccess = { onResult(activity) },
                        onFailed = {},
                        onNotSecurity = { onResult(activity) }
                    )
                }
            }
        }
    )
}

private fun onResult(activity: BaseComposeGooglePayActivity) {
    val task = activity.paymentModel?.getLoadPaymentDataTask(DataHolder.purchaseAmount)
    task?.addOnCompleteListener(activity.paymentDataLauncher::launch)
}

package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.content.Context
import android.util.AttributeSet
import android.widget.LinearLayout
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.ui_components.initAuth

class GooglePayNativeXml: LinearLayout {

    constructor(context: Context?) : super(context) {
        onCreate(context)
    }
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs) {
        onCreate(context)
    }
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        onCreate(context)
    }

    fun onCreate(context: Context?) {
        val view = inflate(context, R.layout.airba_pay_google_pay_button, this)
        val composeView = view.findViewById<ComposeView>(R.id.googlePayComposeView)
        composeView.setContent {
            val activity = LocalContext.current as BaseXmlGooglePayActivity
            val hasGooglePay = activity.paymentModel?.paymentUiState?.collectAsState()
            val coroutineScope = rememberCoroutineScope()

            AndroidView(
                modifier = Modifier.fillMaxWidth(),
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
    }

    private fun onResult(activity: BaseXmlGooglePayActivity) {
        val task = activity.paymentModel?.getLoadPaymentDataTask(DataHolder.purchaseAmount)
        task?.addOnCompleteListener(activity.paymentDataLauncher::launch)
    }
}
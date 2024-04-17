package kz.airbapay.apay_android.ui.pages.test_page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.google.android.gms.wallet.contract.TaskResultContracts
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GooglePayCheckoutViewModel
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GooglePayUtil
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GooglePayViewModel
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

internal class TestComposeGooglePayExternalActivity: ComponentActivity() {

    private val isLoading = mutableStateOf(true)
    private var paymentModel: GooglePayCheckoutViewModel? = null
    private val googlePayViewModel = GooglePayViewModel()
    var coroutineScope: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestSdk(this@TestComposeGooglePayExternalActivity)
        paymentModel = GooglePayCheckoutViewModel(this.application)

        googlePayViewModel.auth(
            activity = this@TestComposeGooglePayExternalActivity,
            onSuccess = {
                isLoading.value = false
                // needShowGooglePayButton = true
            },
            onError = {
                isLoading.value = false
                // needShowGooglePayButton = false
            }
        )

        setContent {
            coroutineScope = rememberCoroutineScope()

            ConstraintLayout {

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

                            this.isEnabled = true

                            setOnClickListener {
                                val task = paymentModel?.getLoadPaymentDataTask(DataHolder.purchaseAmount)
                                task?.addOnCompleteListener(paymentDataLauncher::launch)

                            }
                        }
                    }
                )

                if (isLoading.value) {
                    ProgressBarView()
                }
            }
        }
    }

    private val paymentDataLauncher = this.registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->

        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    googlePayViewModel.processingWalletExternal(
                        activity = this@TestComposeGooglePayExternalActivity,
                        coroutineScope = coroutineScope!!,
                        googlePayToken = it.toJson()
                    )
                }
            }

            else -> {}
        }
    }
}



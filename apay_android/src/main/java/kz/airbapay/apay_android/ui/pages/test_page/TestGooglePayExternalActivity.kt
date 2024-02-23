package kz.airbapay.apay_android.ui.pages.test_page

import android.app.Activity
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import com.google.android.gms.wallet.contract.TaskResultContracts
import com.google.gson.Gson
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.GooglePayTokenResponse
import kz.airbapay.apay_android.data.utils.PaymentsUtil
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.ui.pages.googlepay.AirbaPayGooglePayViewModel
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.CheckoutViewModel
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

internal class TestGooglePayExternalActivity: ComponentActivity() {

    private var paymentModel: CheckoutViewModel? = null
    private val googlePayViewModel = AirbaPayGooglePayViewModel()
    private val isLoading = mutableStateOf(true)

    private val paymentDataLauncher = registerForActivityResult(TaskResultContracts.GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    paymentModel?.setLoadingState(true)
                    paymentModel?.setPaymentData(it)
                    val response = Gson().fromJson(it.toJson(), GooglePayTokenResponse::class.java)
                    val token = response.paymentMethodData?.tokenizationData?.token ?: ""

                    googlePayViewModel.processingWallet(
                        googlePayToken = token,
                        activity = this
                    )
                }
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnCreate(this@TestGooglePayExternalActivity)
        paymentModel = CheckoutViewModel(this.application)

        googlePayViewModel.auth(
            activity = this,
            onError = {
                openErrorPageWithCondition(
                    errorCode = ErrorsCode.error_1.code,
                    activity = this
                )
            },
            onSuccess = {
                isLoading.value = false
            }
        )

        setContent {

            ConstraintLayout {

                AndroidView(
                    modifier = Modifier
                        .height(100.dp)
                        .padding(16.dp),
                    factory = { context ->
                        PayButton(context).apply {
                            this.initialize(
                                ButtonOptions.newBuilder()
                                    .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                                    .setButtonType(ButtonConstants.ButtonType.PLAIN)
                                    .setCornerRadius(8)
                                    .setAllowedPaymentMethods(PaymentsUtil.allowedPaymentMethods().toString())
                                    .build()
                            )
                        }
                    },
                    update = { button ->
                        button.apply {
                            this.isEnabled = true

                            setOnClickListener {
                                val task = paymentModel?.getLoadPaymentDataTask("1500")
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
}

private fun initOnCreate(
    activity: Activity
) {
    AirbaPaySdk.initOnCreate(
        context = activity,
        isProd = false,
        accountId = "77051111111",
        phone = "77051111117",
        shopId = "test-merchant",
        lang = AirbaPaySdk.Lang.RU,
        password = "123456",
        terminalId = "64216e7ccc4a48db060dd689",
        failureCallback = "https://site.kz/failure-clb",
        successCallback = "https://site.kz/success-clb",
        userEmail = "test@test.com",
        colorBrandMain = Color(0xFFFC6B3F),
        autoCharge = 0,
        hideInternalGooglePayButton = true
    )
}

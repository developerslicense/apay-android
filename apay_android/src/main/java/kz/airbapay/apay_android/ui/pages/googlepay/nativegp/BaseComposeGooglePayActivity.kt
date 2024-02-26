package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import com.google.gson.Gson
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.GooglePayTokenResponse
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition

abstract class BaseComposeGooglePayActivity : ComponentActivity() {

    var paymentModel: GooglePayCheckoutViewModel? = null
    private val googlePayViewModel = GooglePayViewModel()

    val paymentDataLauncher = registerForActivityResult(GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    paymentModel?.setLoadingState(true)
                    paymentModel?.setPaymentData(it)
                    val response = Gson().fromJson(it.toJson(), GooglePayTokenResponse::class.java)
                    val token = response.paymentMethodData?.tokenizationData?.token ?: ""

                    googlePayViewModel.processingWallet(
                        activity = this,
                        googlePayToken = token
                    )
                }
            }
            //CommonStatusCodes.CANCELED -> The user canceled
            AutoResolveHelper.RESULT_ERROR -> {
                openErrorPageWithCondition(
                    errorCode = ErrorsCode.error_1.code,
                    activity = this
                )
            }
            CommonStatusCodes.INTERNAL_ERROR -> {
                openErrorPageWithCondition(
                    errorCode = ErrorsCode.error_1.code,
                    activity = this
                )
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentModel = GooglePayCheckoutViewModel(this.application)
    }

    fun authGooglePay(
        onSuccess: () -> Unit,
        onFailed: () -> Unit
    ) {
        googlePayViewModel.auth(
            activity = this,
            onError = onFailed,
            onSuccess = onSuccess
        )
    }
}
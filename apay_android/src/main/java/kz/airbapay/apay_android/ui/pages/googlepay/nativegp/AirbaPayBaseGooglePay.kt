package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.GooglePayMerchantResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition

internal class AirbaPayBaseGooglePay(
    val activity: ComponentActivity
) {

    var paymentModel: GooglePayCheckoutViewModel? = null
    private val googlePayViewModel = GooglePayViewModel()

    private val paymentDataLauncher = activity.registerForActivityResult(GetPaymentDataResult()) { taskResult ->

        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    if (!DataHolder.isProd || DataHolder.enabledLogsForProd) {
                        Log.i("Google Pay result:", it.toJson())
                    }
                    paymentModel?.setLoadingState(true)
                    paymentModel?.setPaymentData(it)
//                    val response = Gson().fromJson(it.toJson(), GooglePayTokenResponse::class.java)
//                    val token = response.paymentMethodData?.tokenizationData?.token ?: ""

                    googlePayViewModel.processingWalletInternal(
                        activity = activity,
                        googlePayToken = it.toJson()
                    )
                }
            }
            //CommonStatusCodes.CANCELED -> The user canceled
            AutoResolveHelper.RESULT_ERROR -> {
                openErrorPageWithCondition(
                    errorCode = ErrorsCode.error_1.code,
                    activity = activity
                )
            }
            CommonStatusCodes.INTERNAL_ERROR -> {
                openErrorPageWithCondition(
                    errorCode = ErrorsCode.error_1.code,
                    activity = activity
                )
            }
        }
    }

    init {
        paymentModel = GooglePayCheckoutViewModel(activity.application)
    }

    fun authGooglePay(
        onSuccess: (GooglePayMerchantResponse) -> Unit,
        onFailed: () -> Unit
    ) {
        googlePayViewModel.auth(
            activity = activity,
            onError = onFailed,
            onSuccess = onSuccess
        )
    }

    fun onResultGooglePay() {
        val task = paymentModel?.getLoadPaymentDataTask(DataHolder.purchaseAmount)
        task?.addOnCompleteListener(paymentDataLauncher::launch)
    }
}
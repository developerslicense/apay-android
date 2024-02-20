package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult

internal abstract class BaseGooglePayActivity : ComponentActivity() {

    var paymentModel: CheckoutViewModel? = null

    val paymentDataLauncher = registerForActivityResult(GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {
                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    paymentModel?.setPaymentData(it)
                }
            }
            //CommonStatusCodes.CANCELED -> The user canceled
            //AutoResolveHelper.RESULT_ERROR -> The API returned an error (it.status: Status)
            //CommonStatusCodes.INTERNAL_ERROR -> Handle other unexpected errors
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        paymentModel = CheckoutViewModel(this.application)
    }
}
package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import com.google.android.gms.common.api.CommonStatusCodes
import com.google.android.gms.wallet.AutoResolveHelper
import com.google.android.gms.wallet.contract.TaskResultContracts.GetPaymentDataResult
import com.google.gson.Gson
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.model.GooglePayTokenResponse
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository

internal abstract class BaseGooglePayActivity : ComponentActivity() {

    var paymentModel: CheckoutViewModel? = null

    val paymentDataLauncher = registerForActivityResult(GetPaymentDataResult()) { taskResult ->
        when (taskResult.status.statusCode) {
            CommonStatusCodes.SUCCESS -> {

                taskResult.result!!.let {
                    Log.i("Google Pay result:", it.toJson())
                    paymentModel?.setLoadingState(true)
                    paymentModel?.setPaymentData(it)
                    val response = Gson().fromJson(it.toJson(), GooglePayTokenResponse::class.java)

                    Repository.paymentsRepository?.startPaymentWallet(
                        googlePayToken = response.paymentMethodData?.tokenizationData?.token ?: "",
                        result = {entryResponse ->

                            if (entryResponse.errorCode != "0") {
                                val error = initErrorsCodeByCode(entryResponse.errorCode?.toInt() ?: 1)
                                openErrorPageWithCondition(
                                    errorCode = error.code,
                                    activity = this
                                )

                            } else if (entryResponse.isSecure3D == true) {
                                openAcquiring(
                                    redirectUrl = entryResponse.secure3D?.action,
                                    activity = this
                                )

                            } else {
                                openSuccess(this)
                            }
                        },
                        error = {

                            if (it.errorBody()?.string()?.contains("invalid pan") == true) {
                                openErrorPageWithCondition(
                                    errorCode = ErrorsCode.error_5002.code,
                                    activity = this
                                )

                            } else {
                                openErrorPageWithCondition(
                                    errorCode = ErrorsCode.error_1.code,
                                    activity = this
                                )
                            }
                        }
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
        paymentModel = CheckoutViewModel(this.application)
    }
}



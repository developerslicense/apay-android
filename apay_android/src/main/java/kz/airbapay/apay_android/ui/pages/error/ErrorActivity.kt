package kz.airbapay.apay_android.ui.pages.error

import android.os.Bundle
import androidx.activity.compose.setContent
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.loggly.Logger
import kz.airbapay.apay_android.ui.pages.BaseActivity

internal class ErrorActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errorCode = intent.getIntExtra(ARG_ERROR_CODE, 1)
        val error = initErrorsCodeByCode(errorCode)

        Logger.log(
            message = "onCreate ErrorActivity. Error code = ${error.code}",
        )

        setContent {
            if (error == ErrorsCode.error_1) {
                ErrorSomethingWrongPage()

            } else if (error.code == ErrorsCode.error_5020.code) {
                if (DataHolder.openCustomPageFinalError != null) {
                    DataHolder.openCustomPageFinalError?.invoke(this)
                } else {
                    ErrorFinalPage()
                }

            } else if (error.code == ErrorsCode.error_5999.code && DataHolder.bankCode?.isNotBlank() == true) {
                ErrorWithInstructionPage(errorCode = error)

            } else {
                ErrorPage(errorCode = error)
            }
        }
    }

    override fun getPageName() = this.localClassName
}
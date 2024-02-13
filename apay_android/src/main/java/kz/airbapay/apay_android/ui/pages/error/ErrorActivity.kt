package kz.airbapay.apay_android.ui.pages.error

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.utils.DataHolder

internal class ErrorActivity: ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val errorCode = intent.getIntExtra(ARG_ERROR_CODE, 1)
        val error = initErrorsCodeByCode(errorCode)

        setContent {
            if (error == ErrorsCode.error_1) {
                ErrorSomethingWrongPage()

            } else if (error.code == ErrorsCode.error_5020.code) {
                if (DataHolder.redirectToCustomFinalErrorPage != null) {
                    DataHolder.redirectToCustomFinalErrorPage?.invoke()
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
}
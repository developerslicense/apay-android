package kz.airbapay.apay_android.ui.pages.error

import androidx.navigation.NavController
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.data.constant.ARG_IS_RETRY
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.constant.routesError
import kz.airbapay.apay_android.data.constant.routesErrorFinal
import kz.airbapay.apay_android.data.constant.routesErrorSomethingWrong
import kz.airbapay.apay_android.data.constant.routesErrorWithInstruction
import kz.airbapay.apay_android.data.utils.DataHolder

internal fun openErrorPageWithCondition(
    errorCode: Int?,
    isRetry: Boolean,
    navController: NavController
) {
    val error = initErrorsCodeByCode(errorCode ?: 1)

    if (error == ErrorsCode.error_1) {
        navController.navigate(routesErrorSomethingWrong)

    } else if (errorCode == ErrorsCode.error_5020.code || errorCode == null) {
        navController.navigate(routesErrorFinal)

    } else if (errorCode == ErrorsCode.error_5999.code && DataHolder.bankName?.isNotBlank() == true) {
        navController.navigate(routesErrorWithInstruction)

    } else {
        navController.navigate(
            routesError
                    + "?$ARG_ERROR_CODE=${errorCode}"
                    + "?$ARG_IS_RETRY=$isRetry",
        )
    }
}

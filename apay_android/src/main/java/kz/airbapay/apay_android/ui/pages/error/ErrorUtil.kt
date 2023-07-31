package kz.airbapay.apay_android.ui.pages.error

import android.content.Context
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode

internal fun openErrorPageWithCondition(
    errorCode: Int?,
    context: Context,
    bankName: String?,
    isRetry: Boolean
) {
  val error = initErrorsCodeByCode(errorCode ?: 1)

  if (error == ErrorsCode.error_1) {
    /*Navigator.pushNamed(
        context,
        routesErrorSomethingWrong,
        arguments: bankName
    )*/

  } else if (errorCode == ErrorsCode.error_5020.code || errorCode == null) {
    /*Navigator.pushNamed(
      context,
      routesErrorFinal,
    )*/

  } else if (errorCode == ErrorsCode.error_5999.code && bankName?.isNotBlank() == true) {
    /*Navigator.pushNamed(
      context,
      routesErrorWithInstruction,
      arguments: bankName
    )*/

  } else {
    /*Navigator.pushNamed(
        context,
        routesError,
        arguments: {
          "errorCode": errorCode.toString(),
          "is_retry": isRetry.toString()
        }
    )*/
  }
}

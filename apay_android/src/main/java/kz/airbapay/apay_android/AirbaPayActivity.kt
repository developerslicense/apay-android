package kz.airbapay.apay_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kz.airbapay.apay_android.ui.pages.success.ErrorFinalPage
import kz.airbapay.apay_android.ui.pages.success.ErrorPage
import kz.airbapay.apay_android.ui.pages.success.ErrorSomethingWrongPage
import kz.airbapay.apay_android.ui.pages.success.SuccessPage
import kz.airbapay.apay_android.ui.resources.ErrorsCode
import kz.airbapay.apay_android.ui.resources.initErrorsCodeByCode

class AirbaPayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = intent.getStringExtra("airba_pay_args")
        Log.i("Arguments for AirbaPay", arguments.orEmpty())

        /*    final Map<String, String?>? args = ModalRoute.of(context)?.settings.arguments as Map<String, String?>?;
   final ErrorsCode error = ErrorsCode.initByCode(int.parse(args?['errorCode'] ?? '1'));
*/
        val errorCode = initErrorsCodeByCode(5002)

        setContent {
            ErrorPage(errorCode)
        }
    }
}
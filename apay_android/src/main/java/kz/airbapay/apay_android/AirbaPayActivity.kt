package kz.airbapay.apay_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kz.airbapay.apay_android.data.model.initGoodsFromString
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.data.utils.getValueFromArguments
import kz.airbapay.apay_android.data.utils.messageLog

class AirbaPayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = intent.getStringExtra("airba_pay_args")
        messageLog("Arguments ${arguments.orEmpty()}")
        initProcessing(arguments.orEmpty())
        /*    final Map<String, String?>? args = ModalRoute.of(context)?.settings.arguments as Map<String, String?>?
   final ErrorsCode error = ErrorsCode.initByCode(int.parse(args?["errorCode"] ?? "1"))
*/
        val errorCode = initErrorsCodeByCode(5999)//5002

        setContent {
            ErrorWithInstructionPage(errorCode, BanksName.kaspi.name)
        }
    }

    private fun initProcessing(
        arguments: String
    ) {
        val resultArgument = arguments.split("?")
        val resultArgumentProducts = arguments.split("good_model")
        messageLog("resultArguments=$resultArgument")
        messageLog("resultArgumentsProducts=$resultArgumentProducts")

        val isProd = getValueFromArguments(resultArgument, "isProd")
        val purchaseAmount = getValueFromArguments(resultArgument, "purchaseAmount") ?: "0"
        val phone = getValueFromArguments(resultArgument, "phone")
        val orderNumber = getValueFromArguments(resultArgument, "orderNumber")
        val invoiceId = getValueFromArguments(resultArgument, "invoiceId")
        val shopId = getValueFromArguments(resultArgument, "shopId")
        val password = getValueFromArguments(resultArgument, "password")
        val terminalId = getValueFromArguments(resultArgument, "terminalId")
        val failureBackUrl = getValueFromArguments(resultArgument, "failureBackUrl")
        val failureCallback = getValueFromArguments(resultArgument, "failureCallback")
        val successBackUrl = getValueFromArguments(resultArgument, "successBackUrl")
        val successCallback = getValueFromArguments(resultArgument, "successCallback")
        val userEmail = getValueFromArguments(resultArgument, "userEmail")
        val lang = getValueFromArguments(resultArgument, "lang")

        val goods = resultArgumentProducts.map {
            initGoodsFromString(it)
        }

        
        DataHolder.accessToken = null
        DataHolder.isProd = isProd.toBoolean()
        DataHolder.baseUrl = if(DataHolder.isProd) "https://ps.airbapay.kz/acquiring-api/sdk"
        else "https://sps.airbapay.kz/acquiring-api/sdk"

        DataHolder.userPhone = phone.orEmpty()
        DataHolder.userEmail = userEmail

        DataHolder.failureBackUrl = failureBackUrl.orEmpty()
        DataHolder.failureCallback = failureCallback.orEmpty()
        DataHolder.successBackUrl = successBackUrl.orEmpty()
        DataHolder.successCallback = successCallback.orEmpty()

        DataHolder.sendTimeout = 60
        DataHolder.connectTimeout = 60
        DataHolder.receiveTimeout = 60
        DataHolder.shopId = shopId.orEmpty()
        DataHolder.password = password.orEmpty()
        DataHolder.terminalId = terminalId.orEmpty()
        DataHolder.purchaseAmount = Money.initString(purchaseAmount).getFormatted()
        DataHolder.orderNumber = orderNumber.orEmpty()
        DataHolder.invoiceId = invoiceId.orEmpty()

        DataHolder.goods = goods

        DataHolder.currentLang = lang ?: AirbaPaySdk.Lang.RU.lang
    }
}


package kz.airbapay.apay_android

import android.content.Context
import android.content.Intent
import android.util.Log
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.bl_components.blAuth
import kz.airbapay.apay_android.ui.bl_components.blCreatePaymentV1
import kz.airbapay.apay_android.ui.pages.startview.StartProcessingActivity
import kz.airbapay.apay_android.ui.resources.ColorsSdk

class AirbaPaySdk {

    enum class Lang(val lang: String) {
        RU("ru"),
        KZ("kz"),
    }

    class Goods(
        @SerializedName("brand")
        val brand: String,  // Брэнд продукта

        @SerializedName("category")
        val category: String, // Категория продукта

        @SerializedName("model")
        val model: String, // Модель продукта

        @SerializedName("quantity")
        val quantity: Int, // Количество в корзине

        @SerializedName("price")
        val price: Long // Цена продукта
    )

    class SettlementPayment( //не обязательный параметр, нужно присылать, если есть необходимость в разделении счетов по компаниям
        @SerializedName("amount")
        val amount: Double,

        @SerializedName("company_id")
        val companyId: String?
    )

    companion object {

        fun initSdk(
            context: Context,
            isProd: Boolean,
            lang: Lang,
            accountId: String,
            phone: String,
            userEmail: String? = null,
            failureCallback: String,
            successCallback: String,
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
            autoCharge: Int = 0,
            enabledLogsForProd: Boolean = false,
            isGooglePayNative: Boolean = false,
            purchaseAmount: Double,
            invoiceId: String,
            orderNumber: String,
            needDisableScreenShot: Boolean = false,

            actionOnCloseProcessing: (Boolean) -> Unit,
            openCustomPageSuccess: (() -> Unit)? = null,
            openCustomPageFinalError: (() -> Unit)? = null,
            renderInStandardFlowGooglePay: Boolean,
            renderInStandardFlowSavedCards: Boolean,
            renderGlobalSecurityCvv: Boolean,
            renderGlobalSecurityBiometry: Boolean
        ) {

            if (colorBrandInversion != null) {
                ColorsSdk.colorBrandInversionMS.value = colorBrandInversion
            }

            if (colorBrandMain != null) {
                ColorsSdk.colorBrandMainMS.value = colorBrandMain
            }

            DataHolder.bankCode = null
            DataHolder.isProd = isProd
            DataHolder.enabledLogsForProd = enabledLogsForProd

            DataHolder.baseUrl = if (DataHolder.isProd) "https://ps.airbapay.kz/acquiring-api/sdk/"
            else "https://sps.airbapay.kz/acquiring-api/sdk/"

            DataHolder.userPhone = phone
            DataHolder.accountId = accountId
            DataHolder.userEmail = userEmail

            DataHolder.failureCallback = failureCallback
            DataHolder.successCallback = successCallback

            DataHolder.sendTimeout = 60
            DataHolder.connectTimeout = 60
            DataHolder.receiveTimeout = 60
            DataHolder.autoCharge = autoCharge

            DataHolder.currentLang = lang.lang
            DataHolder.isGooglePayNative = isGooglePayNative
            DataHolder.needDisableScreenShot = needDisableScreenShot

            DataHolder.purchaseAmount = purchaseAmount
            DataHolder.orderNumber = orderNumber
            DataHolder.invoiceId = invoiceId

            DataHolder.purchaseAmountFormatted.value = Money.initDouble(purchaseAmount).getFormatted()

            DataHolder.openCustomPageSuccess = openCustomPageSuccess
            DataHolder.openCustomPageFinalError = openCustomPageFinalError
            DataHolder.actionOnCloseProcessing = actionOnCloseProcessing

            DataHolder.renderInStandardFlowGooglePay = renderInStandardFlowGooglePay
            DataHolder.renderInStandardFlowSavedCards = renderInStandardFlowSavedCards
            DataHolder.renderGlobalSecurityCvv = renderGlobalSecurityCvv
            DataHolder.renderGlobalSecurityBiometry = renderGlobalSecurityBiometry


            // не переносить
            Repository.initRepositories(context.applicationContext)

        }

       /* fun auth(
            jwt: String,
            paymentId: String,
            onSuccess: (String) -> Unit,
            onError: () -> Unit
        ) {
            blAuth(
                jwt = jwt,
                paymentId = paymentId,
                onSuccess = onSuccess,
                onError = onError
            )
        }*/

        fun authV1(
            terminalId: String,
            shopId: String,
            password: String,
            onSuccess: (String) -> Unit,
            onError: () -> Unit,
            paymentId: String? = null
        ) {
            blAuth(
                password = password,
                terminalId = terminalId,
                shopId = shopId,
                paymentId = paymentId,
                onSuccess = onSuccess,
                onError = onError
            )
        }

        fun createPaymentV1(
            authToken: String,
            goods: List<Goods>,
            settlementPayments: List<SettlementPayment>? = null,
            onSuccess: (String) -> Unit,
            onError: () -> Unit
        ) {
           blCreatePaymentV1(
               authToken = authToken,
               goods = goods,
               settlementPayments = settlementPayments,
               onSuccess = onSuccess,
               onError = onError
           )
        }

        fun standardFlow(context: Context) {
            if (DataHolder.token != null) {
                context.startActivity(Intent(context, StartProcessingActivity::class.java))
            } else {
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                Log.e("AirbaPay", "Нужно предварительно выполнить авторизацию и создание платежа")
            }
        }

    }
}

package kz.airbapay.apay_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.network.repository.Repository
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

    class SettlementPayment(
        @SerializedName("amount")
        val amount: Double,

        @SerializedName("company_id")
        val companyId: String?
    )

    companion object {
        /**
         * @settlementPayments - не обязательный параметр, нужно присылать, если есть необходимость в разделении счетов по компаниям
         * */
        fun initSdk(
            context: Context,
            isProd: Boolean,
            lang: Lang,
            accountId: String,
            phone: String,
            shopId: String,
            password: String,
            terminalId: String,
            failureCallback: String,
            successCallback: String,
            userEmail: String? = null,
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
            autoCharge: Int = 0,
            enabledLogsForProd: Boolean = false,
            isGooglePayNative: Boolean = false,
            hideInternalGooglePayButton: Boolean = false,
            purchaseAmount: Double,
            invoiceId: String,
            orderNumber: String,
            goods: List<Goods>,
            settlementPayments: List<SettlementPayment>? = null, // параметр, нужный, если несколько айдишников компаний
            onProcessingResult: ((Activity, Boolean) -> Unit),
            needDisableScreenShot: Boolean = false
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

            DataHolder.accountId = accountId
            DataHolder.userPhone = phone
            DataHolder.userEmail = userEmail

            DataHolder.failureCallback = failureCallback
            DataHolder.successCallback = successCallback

            DataHolder.sendTimeout = 60
            DataHolder.connectTimeout = 60
            DataHolder.receiveTimeout = 60
            DataHolder.shopId = shopId
            DataHolder.password = password
            DataHolder.terminalId = terminalId
            DataHolder.autoCharge = autoCharge

            DataHolder.currentLang = lang.lang
            DataHolder.isGooglePayNative = isGooglePayNative
            DataHolder.hideInternalGooglePayButton = hideInternalGooglePayButton

            DataHolder.purchaseAmount = purchaseAmount
            DataHolder.orderNumber = orderNumber
            DataHolder.invoiceId = invoiceId
            DataHolder.goods = goods
            DataHolder.settlementPayments = settlementPayments

            DataHolder.purchaseAmountFormatted.value = Money.initDouble(purchaseAmount).getFormatted()

            DataHolder.frontendCallback = onProcessingResult
            DataHolder.needDisableScreenShot = needDisableScreenShot

            // не переносить
            Repository.initRepositories(context.applicationContext)
        }

        fun startAirbaPay(
            activity: Activity,
            redirectToCustomSuccessPage: ((Activity) -> Unit)? = null,
            redirectToCustomFinalErrorPage: ((Activity) -> Unit)? = null
        ) {
            if (DataHolder.baseUrl.isNotEmpty()) {
                DataHolder.redirectToCustomSuccessPage = redirectToCustomSuccessPage
                DataHolder.redirectToCustomFinalErrorPage = redirectToCustomFinalErrorPage
                activity.startActivity(Intent(activity, StartProcessingActivity::class.java))
                activity.finish()
            } else {
                println("Не выполнено initOnCreate")
            }
        }
    }
}

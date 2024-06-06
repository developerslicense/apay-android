package kz.airbapay.apay_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.DisplayMetrics
import android.util.Log
import android.view.WindowManager
import android.widget.Toast
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.model.GooglePayMerchantResponse
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.bl_components.blAuth
import kz.airbapay.apay_android.ui.bl_components.blCreatePayment
import kz.airbapay.apay_android.ui.bl_components.blGetGooglePayMerchantIdAndGateway
import kz.airbapay.apay_android.ui.bl_components.blProcessGooglePay
import kz.airbapay.apay_android.ui.bl_components.saved_cards.blCheckSavedCardNeedCvv
import kz.airbapay.apay_android.ui.bl_components.saved_cards.blDeleteCard
import kz.airbapay.apay_android.ui.bl_components.saved_cards.blGetSavedCards
import kz.airbapay.apay_android.ui.pages.external_webview.ExternalStandardFlowWebViewActivity
import kz.airbapay.apay_android.ui.pages.startview.StartProcessingActivity
import kz.airbapay.apay_android.ui.resources.ColorsSdk

class AirbaPaySdk {

    enum class Lang(val lang: String) {
        RU("ru"),
        KZ("kz"),
    }

    class CreatePaymentResult(
        var token: String? = null,
        var paymentId: String? = null
    )

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
            phone: String,
            userEmail: String? = null,
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
            enabledLogsForProd: Boolean = false,
            needDisableScreenShot: Boolean = false,

            actionOnCloseProcessing: (activity: Activity, paymentSubmittingResult: Boolean) -> Unit,
            openCustomPageSuccess: ((Activity) -> Unit)? = null,
            openCustomPageFinalError: ((Activity) -> Unit)? = null
        ) {

            try {
                val wm = context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
                    DataHolder.height = wm.currentWindowMetrics.bounds.height()
                    DataHolder.width = wm.currentWindowMetrics.bounds.width()

                } else {
                    val displaymetrics = DisplayMetrics()
                    wm.defaultDisplay.getMetrics(displaymetrics)
                    DataHolder.height = displaymetrics.heightPixels
                    DataHolder.width = displaymetrics.widthPixels
                }
            } catch (e: Exception) {}

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
            DataHolder.userEmail = userEmail

            DataHolder.sendTimeout = 60
            DataHolder.connectTimeout = 60
            DataHolder.receiveTimeout = 60

            DataHolder.currentLang = lang.lang
            DataHolder.needDisableScreenShot = needDisableScreenShot

            DataHolder.openCustomPageSuccess = openCustomPageSuccess
            DataHolder.openCustomPageFinalError = openCustomPageFinalError
            DataHolder.actionOnCloseProcessing = actionOnCloseProcessing

            DataHolder.renderSecurityCvv = null
            DataHolder.renderSecurityBiometry = null
            DataHolder.renderSavedCards = null
            DataHolder.renderGooglePay = null


            // не переносить
            Repository.initRepositories(context.applicationContext)

        }


        // Auth

        fun authPassword(
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

        fun authJwt(
            jwt: String,
            onSuccess: () -> Unit,
            onError: () -> Unit
        ) {
            DataHolder.token = jwt
            Repository.paymentsRepository?.getPaymentInfo(
                result = { onSuccess() },
                error = { onError() }
            )
        }

        // Create payment

        fun createPayment(
            authToken: String,
            failureCallback: String,
            successCallback: String,
            purchaseAmount: Double,
            accountId: String,
            invoiceId: String,
            orderNumber: String,
            onSuccess: (CreatePaymentResult) -> Unit,
            onError: () -> Unit,
            renderSecurityCvv: Boolean? = null,
            renderSecurityBiometry: Boolean? = null,
            renderGooglePay: Boolean? = null,
            renderSavedCards: Boolean? = null,
            autoCharge: Int = 0,
            goods: List<Goods>? = null,
            settlementPayments: List<SettlementPayment>? = null
        ) {
            DataHolder.accountId = accountId

            blCreatePayment(
                authToken = authToken,
                failureCallback = failureCallback,
                successCallback = successCallback,
                autoCharge = autoCharge,
                purchaseAmount = purchaseAmount,
                orderNumber = orderNumber,
                invoiceId = invoiceId,
                goods = goods,
                settlementPayments = settlementPayments,
                renderGooglePay = renderGooglePay,
                renderSavedCards = renderSavedCards,
                renderSecurityBiometry = renderSecurityBiometry,
                renderSecurityCvv = renderSecurityCvv,
                onSuccess = onSuccess,
                onError = onError
            )
        }

        // Standard flow

        fun standardFlow(
            context: Context,
            isGooglePayNative: Boolean = false
        ) {
            if (DataHolder.token != null) {
                DataHolder.isGooglePayNative = isGooglePayNative
                context.startActivity(Intent(context, StartProcessingActivity::class.java))
            } else {
                Toast.makeText(context, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                Log.e("AirbaPay", "Нужно предварительно выполнить авторизацию и создание платежа")
            }
        }

        fun standardFlowWebView(
            context: Context,
            onError: () -> Unit
        ) {
            if (DataHolder.token != null) {
                Repository.paymentsRepository?.getPaymentInfo(
                    result = { payformUrl ->
                        val intent = Intent(context, ExternalStandardFlowWebViewActivity::class.java)
                        intent.putExtra(ARG_ACTION, payformUrl)
                        context.startActivity(intent)
                    },
                    error = { onError() }
                )
            } else {
                print("AirbaPay. Нужно предварительно выполнить авторизацию и создание платежа")
            }
        }

        // GooglePay

        fun getGooglePayMerchantIdAndGateway(
            onSuccess: (GooglePayMerchantResponse) -> Unit,
            onError: () -> Unit
        ) {
            blGetGooglePayMerchantIdAndGateway(onSuccess, onError)
        }

        fun processExternalGooglePay(
            googlePayToken: String,
            activity: Activity
        ) {
            blProcessGooglePay(googlePayToken, activity)
        }

        // Cards

        fun getCards(
            onSuccess: (List<BankCard>) -> Unit,
            onNoCards: () -> Unit
        ) {
            blGetSavedCards(onSuccess, onNoCards)
        }

        fun paySavedCard(
            activity: Activity,
            bankCard: BankCard,
            isLoading: (Boolean) -> Unit,
            onError: () -> Unit
        ) {
            Repository.paymentsRepository?.getPaymentInfo(
                result = {
                    blCheckSavedCardNeedCvv(activity, bankCard, isLoading, onError)
                },
                error = {
                    openErrorPageWithCondition(
                        errorCode = ErrorsCode.error_1.code,
                        activity = activity
                    )
                }
            )
        }

        fun deleteCard(
            cardId: String,
            onSuccess: () -> Unit,
            onError: () -> Unit
        ) {
            blDeleteCard(cardId, onSuccess, onError)
        }
    }
}

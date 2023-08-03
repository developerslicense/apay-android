package kz.airbapay.apay_android

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
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
        val amount: Long,

        @SerializedName("company_id")
        val companyId: String?
    )

    companion object {
        fun startProcessing(
            context: Context,
            isProd: Boolean,
            lang: Lang,
            purchaseAmount: Long,
            phone: String,
            invoiceId: String,
            orderNumber: String,
            shopId: String,
            password: String,
            terminalId: String,
            failureBackUrl: String,
            failureCallback: String,
            successBackUrl: String,
            successCallback: String,
            userEmail: String?,
            goods: List<Goods>,
            settlementPayments: List<SettlementPayment>?,
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
        ) {

            if (colorBrandInversion != null) {
                ColorsSdk.colorBrandInversionMS.value = colorBrandInversion
            }

            if (colorBrandMain != null) {
                ColorsSdk.colorBrandMainMS.value = colorBrandMain
            }

            DataHolder.accessToken = null
            DataHolder.isProd = isProd
            DataHolder.baseUrl = if (DataHolder.isProd) "https://ps.airbapay.kz/acquiring-api/sdk/"
            else "https://sps.airbapay.kz/acquiring-api/sdk/"

            DataHolder.userPhone = phone
            DataHolder.userEmail = userEmail

            DataHolder.failureBackUrl = failureBackUrl
            DataHolder.failureCallback = failureCallback
            DataHolder.successBackUrl = successBackUrl
            DataHolder.successCallback = successCallback

            DataHolder.sendTimeout = 60
            DataHolder.connectTimeout = 60
            DataHolder.receiveTimeout = 60
            DataHolder.shopId = shopId
            DataHolder.password = password
            DataHolder.terminalId = terminalId
            DataHolder.purchaseAmount = Money.initLong(purchaseAmount).getFormatted()
            DataHolder.orderNumber = orderNumber
            DataHolder.invoiceId = invoiceId

            DataHolder.goods = goods
            DataHolder.settlementPayments = settlementPayments

            DataHolder.currentLang = lang.lang

            val intent = Intent(context, AirbaPayActivity::class.java)
            context.startActivity(intent)
        }
    }
}


package kz.airbapay.apay_android

import android.content.Context
import android.content.Intent
import androidx.compose.ui.graphics.Color
import kz.airbapay.apay_android.ui.resources.ColorsSdk

class AirbaPaySdk {

    enum class Lang(val lang: String) {
        RU("ru"),
        KZ("kz"),
    }

    class Goods(
        val brand: String,  // Брэнд продукта
        val category: String, // Категория продукта
        val model: String, // Модель продукта
        val quantity: Int, // Количество в корзине
        val price: Int // Цена продукта
    ) 

   companion object {
        fun startProcessing(
            context: Context,
            isProd: Boolean,
            lang: Lang,
            purchaseAmount: Int,
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
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
        ) {

            if(colorBrandInversion != null) {
                ColorsSdk.colorBrandInversionMS.value = colorBrandInversion
            }

            if(colorBrandMain != null) {
                ColorsSdk.colorBrandMainMS.value = colorBrandMain
            }

            val sb = StringBuilder()
            sb.append("isProd=$isProd")
            sb.append("?")

            sb.append("purchaseAmount=$purchaseAmount")
            sb.append("?")

            sb.append("phone=$phone")
            sb.append("?")

            sb.append("lang=${lang.lang}")
            sb.append("?")

            sb.append("invoiceId=$invoiceId")
            sb.append("?")

            sb.append("orderNumber=$orderNumber")
            sb.append("?")

            sb.append("shopId=$shopId")
            sb.append("?")

            sb.append("password=$password")
            sb.append("?")

            sb.append("terminalId=$terminalId")
            sb.append("?")

            sb.append("failureBackUrl=$failureBackUrl")
            sb.append("?")

            sb.append("failureCallback=$failureCallback")
            sb.append("?")

            sb.append("successBackUrl=$successBackUrl")
            sb.append("?")

            sb.append("successCallback=$successCallback")
            sb.append("?")

            if (!userEmail.isNullOrBlank()) { // Если емейла нет, то не нужно отправлять
                sb.append("userEmail=$userEmail")
                sb.append("?")
            }

            goods.forEach { good ->
                sb.append("good_model=${good.model}")
                sb.append("?")
                sb.append("good_quantity=${good.quantity}")
                sb.append("?")
                sb.append("good_brand=${good.brand}")
                sb.append("?")
                sb.append("good_price=${good.price}")
                sb.append("?")
                sb.append("good_category=${good.category}")
                sb.append("?")
            }

            val intent = Intent(context, AirbaPayActivity::class.java)
                .putExtra("airba_pay_args", sb.toString())
                .putExtra("route", "/")

            context.startActivity(intent)
        }
   }
}


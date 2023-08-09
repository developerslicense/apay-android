package kz.airbapay.apay_android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.ui.pages.dialog.BottomSheetStartProcessing
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
        /**
         * @settlementPayments - не обязательный параметр, нужно присылать, если есть необходимость в разделении счетов по компаниям
         * */
        fun initOnCreate(
            isProd: Boolean,
            lang: Lang,
            phone: String,
            userEmail: String?,
            shopId: String,
            password: String,
            terminalId: String,
            needShowSdkSuccessPage: Boolean,
            failureCallback: String,
            successCallback: String,
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
        ) {

            if (colorBrandInversion != null) {
                ColorsSdk.colorBrandInversionMS.value = colorBrandInversion
            }

            if (colorBrandMain != null) {
                ColorsSdk.colorBrandMainMS.value = colorBrandMain
            }

            DataHolder.bankName = null
            DataHolder.accessToken = null
            DataHolder.isProd = isProd
            DataHolder.needShowSdkSuccessPage = needShowSdkSuccessPage
            DataHolder.baseUrl = if (DataHolder.isProd) "https://ps.airbapay.kz/acquiring-api/sdk/"
            else "https://sps.airbapay.kz/acquiring-api/sdk/"

            DataHolder.userPhone = phone
            DataHolder.userEmail = userEmail

            DataHolder.failureBackUrl = "https://site.kz/failure" // не нужно
            DataHolder.failureCallback = failureCallback
            DataHolder.successBackUrl = "https://site.kz/success"// не нужно
            DataHolder.successCallback = successCallback

            DataHolder.sendTimeout = 60
            DataHolder.connectTimeout = 60
            DataHolder.receiveTimeout = 60
            DataHolder.shopId = shopId
            DataHolder.password = password
            DataHolder.terminalId = terminalId

            DataHolder.currentLang = lang.lang
        }

        fun initProcessing(
            purchaseAmount: Long,
            invoiceId: String,
            orderNumber: String,
            goods: List<Goods>,
            settlementPayments: List<SettlementPayment>?,
        ) {
            DataHolder.purchaseAmount = purchaseAmount.toString()
            DataHolder.orderNumber = orderNumber
            DataHolder.invoiceId = invoiceId
            DataHolder.goods = goods
            DataHolder.settlementPayments = settlementPayments

            DataHolder.purchaseAmountFormatted.value = Money.initLong(purchaseAmount).getFormatted()
        }

        /**
         * Вызов xml BottomSheet Dialog Fragment
         * */
       /* fun modalBottomSheetProcessingXml(
            fragmentManager: FragmentManager
        ) {
            val bottomSheet = BottomSheetFragmentStartProcessing()
            bottomSheet.show(fragmentManager, "AirbaPay")
        }*/
    }
}

/**
 * Первый вариант имплементации Compose. Здесь все выполняется на стороне sdk
 * */
@Composable
fun AirbaPaySdkModalBottomSheetProcessingCompose(
    content: @Composable (actionShowBottomSheet: () -> Unit) -> Unit
) {

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    val coroutineScope = rememberCoroutineScope()

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = ColorsSdk.transparent,
        sheetContent = {
            BottomSheetStartProcessing(
                actionClose = { coroutineScope.launch { sheetState.hide() } },
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        content {
            coroutineScope.launch {
                sheetState.show()
            }
        }
    }
}

/**
 * Второй вариант имплементации Compose. Здесь все выполняется на стороне клиента
 * */
@Composable
fun AirbaPaySdkModalProcessingCompose() {

    BottomSheetStartProcessing(
        actionClose = {},
        isBottomSheetType = false
    )
}
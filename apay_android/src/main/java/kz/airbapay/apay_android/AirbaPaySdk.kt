package kz.airbapay.apay_android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.fragment.app.FragmentManager
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.ui.pages.dialog.BottomSheetFragmentStartProcessing
import kz.airbapay.apay_android.ui.pages.dialog.DialogStartProcessing
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
        fun initProcessing(
            isProd: Boolean,
            lang: Lang,
            purchaseAmount: Long,
            phone: String,
            invoiceId: String,
            orderNumber: String,
            shopId: String,
            password: String,
            terminalId: String,
            needShowSdkSuccessPage: Boolean,
            failureCallback: String,
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
            DataHolder.purchaseAmount = Money.initLong(purchaseAmount).getFormatted()
            DataHolder.orderNumber = orderNumber
            DataHolder.invoiceId = invoiceId

            DataHolder.goods = goods
            DataHolder.settlementPayments = settlementPayments

            DataHolder.currentLang = lang.lang
        }

        fun modalBottomSheetProcessingXml(
            fragmentManager: FragmentManager
        ) {
            val bottomSheet = BottomSheetFragmentStartProcessing()
            bottomSheet.show(fragmentManager, "AirbaPay")
        }
    }
}

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
    val purchaseAmount = rememberSaveable {
        mutableStateOf("")
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = ColorsSdk.transparent,
        sheetContent = {
            DialogStartProcessing(
                actionClose = { coroutineScope.launch { sheetState.hide() } },
                purchaseAmount = purchaseAmount.value
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        content {
            purchaseAmount.value = DataHolder.purchaseAmount
            coroutineScope.launch {
                sheetState.show()
            }
        }
    }
}
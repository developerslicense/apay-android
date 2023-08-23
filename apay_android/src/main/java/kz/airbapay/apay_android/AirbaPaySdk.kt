package kz.airbapay.apay_android

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.google.gson.annotations.SerializedName
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.AirbaPayBiometric
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.Money
import kz.airbapay.apay_android.ui.pages.dialog.StartProcessingView
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
            shopId: String,
            password: String,
            terminalId: String,
            failureCallback: String,
            successCallback: String,
            needShowSdkSuccessPage: Boolean = true,
            userEmail: String? = null,
            colorBrandMain: Color? = null,
            colorBrandInversion: Color? = null,
        ) {

            if (colorBrandInversion != null) {
                ColorsSdk.colorBrandInversionMS.value = colorBrandInversion
            }

            if (colorBrandMain != null) {
                ColorsSdk.colorBrandMainMS.value = colorBrandMain
            }

            DataHolder.bankCode = null
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
            settlementPayments: List<SettlementPayment>? = null // параметр, нужный, если несколько айдишников компаний
        ) {
            DataHolder.purchaseAmount = purchaseAmount.toString()
            DataHolder.orderNumber = orderNumber
            DataHolder.invoiceId = invoiceId
            DataHolder.goods = goods
            DataHolder.settlementPayments = settlementPayments

            DataHolder.purchaseAmountFormatted.value = Money.initLong(purchaseAmount).getFormatted()
        }
    }
}

/**
 * Первый вариант имплементации Compose. Здесь все выполняется на стороне sdk
 * */
@Composable
fun AirbaPaySdkProcessingBottomSheet(
    content: @Composable (actionShowBottomSheet: () -> Unit) -> Unit
) {

    val coroutineScope = rememberCoroutineScope()
    val context = LocalContext.current
    val airbaPayBiometric = AirbaPayBiometric(context)

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
        skipHalfExpanded = true
    )

    val isAuthenticated = rememberSaveable {
        mutableStateOf(false)
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = ColorsSdk.transparent,
        sheetContent = {
            StartProcessingView(
                actionClose = { coroutineScope.launch { sheetState.hide() } },
                isAuthenticated = isAuthenticated
            )
        },
        modifier = Modifier.fillMaxSize()
    ) {
        content {
            airbaPayBiometric.authenticate(
                onSuccess = {
                    isAuthenticated.value = true
                    coroutineScope.launch { sheetState.show() }
                },
                onError = {
                    coroutineScope.launch { sheetState.show() }
                }
            )
        }
    }
}

/**
 * Второй вариант имплементации Compose. Здесь все выполняется на стороне клиента
 * */
@Composable
fun AirbaPaySdkProcessingView(
    actionOnLoadingCompleted: () -> Unit = {},
    needShowProgressBar: Boolean = true,
    backgroundColor: Color = ColorsSdk.bgBlock
) {

    val context = LocalContext.current
    val airbaPayBiometric = AirbaPayBiometric(context)

    val isAuthenticated = rememberSaveable {
        mutableStateOf(false)
    }

    StartProcessingView(
        needShowProgressBar = needShowProgressBar,
        actionClose = {},
        actionOnLoadingCompleted = actionOnLoadingCompleted,
        isBottomSheetType = false,
        backgroundColor = backgroundColor,
        isAuthenticated = isAuthenticated
    )

    LaunchedEffect("Authenticate") {
        airbaPayBiometric.authenticate(
            onSuccess = {
                isAuthenticated.value = true
            },
            onError = {}
        )
    }
}
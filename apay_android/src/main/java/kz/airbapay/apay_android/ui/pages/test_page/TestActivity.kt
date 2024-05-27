package kz.airbapay.apay_android.ui.pages.test_page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.pages.home.presentation.SwitchedView
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import java.util.Date

class TestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scrollState = rememberScrollState()

            val autoCharge = remember { mutableStateOf(false) }
            val renderGlobalSecurityCvv = remember { mutableStateOf(DataHolder.renderGlobalSecurityCvv) }
            val renderGlobalSecurityBiometry = remember { mutableStateOf(DataHolder.renderGlobalSecurityBiometry) }
            val renderInStandardFlowSavedCards = remember { mutableStateOf(DataHolder.renderInStandardFlowSavedCards) }
            val renderInStandardFlowGooglePay = remember { mutableStateOf(DataHolder.renderInStandardFlowGooglePay) }
            val nativeGooglePay = remember { mutableStateOf(DataHolder.isGooglePayNative) }
            val needDisableScreenShot = remember { mutableStateOf(DataHolder.needDisableScreenShot) }

            val isLoading = remember { mutableStateOf(false) }

            ConstraintLayout {


                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "Тестовые карты \n 4111 1111 1111 1616 cvv 333 \n " +
                                "4111 1111 1111 1111 cvv 123  \n" +
                                "3411 1111 1111 111 cvv 7777",
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            testInitSdk(
                                activity = this@TestActivity,
                                autoCharge = if (autoCharge.value) 1 else 0,
                                nativeGooglePay = nativeGooglePay.value,
                                needDisableScreenShot = needDisableScreenShot.value,
                                renderGlobalSecurityCvv = renderGlobalSecurityCvv.value,
                                renderGlobalSecurityBiometry = renderGlobalSecurityBiometry.value,
                                renderInStandardFlowSavedCards = renderInStandardFlowSavedCards.value,
                                renderInStandardFlowGooglePay = renderInStandardFlowGooglePay.value
                            )

                            onStandardFlowPassword(
                                isLoading = isLoading,
                                onSuccess = { AirbaPaySdk.standardFlow(this@TestActivity) }
                            )

                        }
                    ) {
                        Text("Стандартный флоу")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {

                            testInitSdk(
                                activity = this@TestActivity,
                                autoCharge = if (autoCharge.value) 1 else 0,
                                nativeGooglePay = nativeGooglePay.value,
                                needDisableScreenShot = needDisableScreenShot.value,
                                renderGlobalSecurityCvv = renderGlobalSecurityCvv.value,
                                renderGlobalSecurityBiometry = renderGlobalSecurityBiometry.value,
                                renderInStandardFlowSavedCards = renderInStandardFlowSavedCards.value,
                                renderInStandardFlowGooglePay = renderInStandardFlowGooglePay.value
                            )

                            startActivity(
                                Intent(
                                    this@TestActivity,
                                    TestGooglePayExternalActivity::class.java
                                )
                            )
                        }
                    ) {
                        Text("Тест внешнего API GooglePay")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            testInitSdk(
                                activity = this@TestActivity,
                                autoCharge = if (autoCharge.value) 1 else 0,
                                nativeGooglePay = nativeGooglePay.value,
                                needDisableScreenShot = needDisableScreenShot.value,
                                renderGlobalSecurityCvv = renderGlobalSecurityCvv.value,
                                renderGlobalSecurityBiometry = renderGlobalSecurityBiometry.value,
                                renderInStandardFlowSavedCards = renderInStandardFlowSavedCards.value,
                                renderInStandardFlowGooglePay = renderInStandardFlowGooglePay.value
                            )

                            startActivity(
                                Intent(
                                    this@TestActivity,
                                    TestCardsExternalActivity::class.java
                                )
                            )
                        }
                    ) {
                        Text("Тест внешнего API сохраненных карт")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isLoading.value = true

                            testInitSdk(activity = this@TestActivity)

                            AirbaPaySdk.auth(
                                onSuccess = { token ->
                                    Repository.cardRepository?.getCards(
                                        accountId = DataHolder.accountId,
                                        result = {
                                            it.forEach {
                                                Repository.cardRepository?.deleteCard(
                                                    cardId = it.id ?: "",
                                                    result = { isLoading.value = false },
                                                    error = { isLoading.value = false }
                                                )
                                            }
                                        },
                                        error = { isLoading.value = false }
                                    )
                                },
                                onError = {},
                                shopId = "test-baykanat", //"airbapay-mfo", //
                                password = "baykanat123!", //"MtTh37TLV7", //
                                terminalId = "65c5df69e8037f1b451a0594",//"659e79e279a508566e35d299", //
                            )
                        }
                    )
                    {
                        Text("Удалить привязанные карты")
                    }


                    SwitchedView("Есть Сохраненные карты", renderInStandardFlowSavedCards)
                    SwitchedView("Есть GooglePay", renderInStandardFlowGooglePay)
                    SwitchedView("Есть CVV", renderGlobalSecurityCvv)
                    SwitchedView("Есть Биометрия", renderGlobalSecurityBiometry)
                    SwitchedView("Нативный GooglePay", nativeGooglePay)
                    SwitchedView("Блокировать скриншот", needDisableScreenShot)
                    SwitchedView("AutoCharge 0 (off) / 1 (on)", autoCharge)
                }

                if (isLoading.value) {
                    ProgressBarView()
                }
            }
        }
    }
}

internal fun Context.onStandardFlowPassword(
    isLoading: MutableState<Boolean>,
    onSuccess: () -> Unit
) {
    isLoading.value = true
    AirbaPaySdk.auth(
        onSuccess = { token ->
            val goods = listOf(
                AirbaPaySdk.Goods(
                    model = "Чай Tess Banana Split черный 20 пирамидок",
                    brand = "Tess",
                    category = "Черный чай",
                    quantity = 1,
                    price = 1000
                ),
                AirbaPaySdk.Goods(
                    model = "Чай Tess Green",
                    brand = "Tess",
                    category = "Green чай",
                    quantity = 1,
                    price = 500
                )
            )

            val settlementPayment = listOf(
                AirbaPaySdk.SettlementPayment(
                    amount = 1000.0,
                    companyId = "210840019439"
                ),
                AirbaPaySdk.SettlementPayment(
                    amount = 500.45,
                    companyId = "254353"
                )
            )

            AirbaPaySdk.createPayment(
                authToken = token,
                onSuccess = { paymentId ->
                    onSuccess()
                },
                onError = {
                    isLoading.value = false
                    Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                },
//                goods = goods,
//                settlementPayments = settlementPayment
            )
        },
        onError = {
            isLoading.value = false
            Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
        },
        shopId = "test-baykanat", //"airbapay-mfo", //
        password = "baykanat123!", //"MtTh37TLV7", //
        terminalId = "65c5df69e8037f1b451a0594",//"659e79e279a508566e35d299", //
        paymentId = null
    )
}

internal fun testInitSdk(
    activity: Activity,
    autoCharge: Int = 0,
    nativeGooglePay: Boolean = true,
    needDisableScreenShot: Boolean = false,
    renderInStandardFlowGooglePay: Boolean = true,
    renderInStandardFlowSavedCards: Boolean = true,
    renderGlobalSecurityCvv: Boolean = true,
    renderGlobalSecurityBiometry: Boolean = true
) {
    DataHolder.hasSavedCards = false

    val someInvoiceId = Date().time
    val someOrderNumber = Date().time

    AirbaPaySdk.initSdk(
        enabledLogsForProd = false, //todo этот же параметр отвечает за отправку логов в дебаг сборке, если == true
        context = activity,
        isProd = false, //true, //
        accountId = "77061111112",//"77051111111",
        phone = "77061111112",//"77051111117",
        lang = AirbaPaySdk.Lang.RU,
        failureCallback = "https://site.kz/failure-clb",
        successCallback = "https://site.kz/success-clb",
        userEmail = "test@test.com",
        colorBrandMain = Color(0xFFFC6B3F),
        autoCharge = autoCharge,
        purchaseAmount = 1500.45,
        isGooglePayNative = nativeGooglePay,
        invoiceId = someInvoiceId.toString(),
        orderNumber = someOrderNumber.toString(),
        actionOnCloseProcessing = { _activity, paymentSubmittingResult ->
            if (paymentSubmittingResult) {
                Log.e("AirbaPaySdk", "initProcessing success");
            } else {
                Log.e("AirbaPaySdk", "initProcessing error");
            }
            _activity.startActivity(Intent(_activity, TestActivity::class.java))
            _activity.finish()
        },
        needDisableScreenShot = needDisableScreenShot,
//        openCustomPageSuccess = {  context.startActivity(Intent(context, CustomSuccessActivity::java.class)) },
        renderInStandardFlowGooglePay = renderInStandardFlowGooglePay,
        renderInStandardFlowSavedCards = renderInStandardFlowSavedCards,
        renderGlobalSecurityBiometry = renderGlobalSecurityBiometry,
        renderGlobalSecurityCvv = renderGlobalSecurityCvv
    )
}

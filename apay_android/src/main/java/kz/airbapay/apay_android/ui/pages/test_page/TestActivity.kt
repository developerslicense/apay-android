package kz.airbapay.apay_android.ui.pages.test_page

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.pages.home.presentation.SwitchedView
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.edit_text.core.ViewEditText
import java.util.Date

var isRenderSecurityCvv: Boolean? = null
var isRenderSecurityBiometry: Boolean? = null
var isRenderSavedCards: Boolean? = null
var isRenderGooglePay: Boolean? = null

class TestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scrollState = rememberScrollState()

            val autoCharge = remember { mutableStateOf(false) }

            val showDropdownRenderSecurityCvv = remember { mutableStateOf(false) }
            val showDropdownRenderSecurityBiometry = remember { mutableStateOf(false) }
            val showDropdownRenderSavedCards = remember { mutableStateOf(false) }
            val showDropdownRenderGooglePay = remember { mutableStateOf(false) }

            val renderSecurityCvv: MutableState<Boolean?> = remember { mutableStateOf(isRenderSecurityCvv) }
            val renderSecurityBiometry: MutableState<Boolean?> = remember { mutableStateOf(isRenderSecurityBiometry) }
            val renderSavedCards: MutableState<Boolean?> = remember { mutableStateOf(isRenderSavedCards) }
            val renderGooglePay: MutableState<Boolean?> = remember { mutableStateOf(isRenderGooglePay) }

            val nativeGooglePay = remember { mutableStateOf(DataHolder.isGooglePayNative) }
            val needDisableScreenShot = remember { mutableStateOf(DataHolder.needDisableScreenShot) }

            val isLoading = remember { mutableStateOf(false) }

            val tokenText = remember { mutableStateOf(TextFieldValue("")) }

            val context = LocalContext.current

            ConstraintLayout {

                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isLoading.value = true

                            testInitSdk(this@TestActivity)

                            AirbaPaySdk.authPassword(
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

                    Text(
                        text = "Номера тестовых карт, которые можно использовать \n " +
                                "4111 1111 1111 1616 cvv 333 \n " +
                                "4111 1111 1111 1111 cvv 123  \n" +
                                "3411 1111 1111 111 cvv 7777",
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        modifier = Modifier
                            .padding(top = 30.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isRenderSecurityCvv = renderSecurityCvv.value
                            isRenderSecurityBiometry = renderSecurityBiometry.value
                            isRenderSavedCards = renderSavedCards.value
                            isRenderGooglePay = renderGooglePay.value

                            testInitSdk(
                                activity = this@TestActivity,
                                needDisableScreenShot = needDisableScreenShot.value
                            )

                            onStandardFlowPassword(
                                autoCharge = if (autoCharge.value) 1 else 0,
                                isLoading = isLoading,
                                onSuccess = {
                                    AirbaPaySdk.standardFlow(
                                        context = this@TestActivity,
                                        isGooglePayNative = nativeGooglePay.value
                                    )
                                },
                                renderSecurityCvv = renderSecurityCvv.value,
                                renderSecurityBiometry = renderSecurityBiometry.value,
                                renderSavedCards = renderSavedCards.value,
                                renderGooglePay = renderGooglePay.value
                            )

                        }
                    ) {
                        Text("Стандартный флоу Password")
                    }


                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            testInitSdk(this@TestActivity)

                            val intent = Intent(
                                this@TestActivity,
                                TestGooglePayExternalActivity::class.java
                            )
                            startActivity(intent)
                        }
                    ) {
                        Text("Тест внешнего API GooglePay PASSWORD")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isRenderSecurityCvv = renderSecurityCvv.value
                            isRenderSecurityBiometry = renderSecurityBiometry.value

                            testInitSdk(this@TestActivity)
                            val intent = Intent(
                                this@TestActivity,
                                TestCardsExternalActivity::class.java
                            )
                            startActivity(intent)
                        }
                    ) {
                        Text("Тест внешнего API сохраненных карт PASSWORD")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isRenderSecurityCvv = renderSecurityCvv.value
                            isRenderSecurityBiometry = renderSecurityBiometry.value

                            testInitSdk(this@TestActivity)
                            onStandardFlowPassword(
                                autoCharge = if (autoCharge.value) 1 else 0,
                                isLoading = isLoading,
                                onSuccess = {
                                    AirbaPaySdk.standardFlowWebView(
                                        context = this@TestActivity,
                                        onError = { isLoading.value = false },
                                        shouldOverrideUrlLoading = { obj ->
                                            when {
                                                obj.isCallbackSuccess -> {
                                                    openSuccess(this@TestActivity)
                                                    return@standardFlowWebView true
                                                }
                                                obj.isCallbackBackToApp -> {
                                                    DataHolder.actionOnCloseProcessing?.invoke(this@TestActivity, false)
                                                    return@standardFlowWebView true
                                                }

                                                else -> obj.webView?.loadUrl(obj.url ?: "")
                                            }

                                            return@standardFlowWebView false
                                        }
                                    )
                                },
                                renderSecurityCvv = renderSecurityCvv.value,
                                renderSecurityBiometry = renderSecurityBiometry.value,
                                renderSavedCards = renderSavedCards.value,
                                renderGooglePay = renderGooglePay.value
                            )
                        }
                    ) {
                        Text("Стандартный флоу через вебвью")
                    }

                    Text(
                        text = "Все нижние варианты требуют предварительно сгенерировать " +
                                "или вставить JWT в поле ввода. " +
                                "\nМожно сгенерировать, скопировать, " +
                                "'убить' приложение, занова запустить и вставить из буффера",
                        modifier = Modifier.padding(10.dp).padding(top = 30.dp)
                    )

                    val tokenFocusRequester = FocusRequester()
                    val e2 = remember { mutableStateOf<String?>(null) }
                    ViewEditText(
                        text = tokenText,
                        isDateExpiredMask = true,
                        errorTitle = e2,
                        focusRequester = tokenFocusRequester,
                        placeholder = "JWT",
                        keyboardActions = KeyboardActions(),
                        modifierRoot = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(top = 10.dp),
                        actionOnTextChanged = {}
                    )

                    Button(
                        modifier = Modifier
                            .padding(top = 10.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isRenderSecurityCvv = renderSecurityCvv.value
                            isRenderSecurityBiometry = renderSecurityBiometry.value
                            isRenderSavedCards = renderSavedCards.value
                            isRenderGooglePay = renderGooglePay.value

                            testInitSdk(
                                activity = this@TestActivity,
                                needDisableScreenShot = needDisableScreenShot.value
                            )

                            onStandardFlowPassword(
                                autoCharge = if (autoCharge.value) 1 else 0,
                                isLoading = isLoading,
                                onSuccess = {
                                    tokenText.value = TextFieldValue(it)
                                    DataHolder.token = null
                                    isLoading.value = false
                                },
                                renderSecurityCvv = renderSecurityCvv.value,
                                renderSecurityBiometry = renderSecurityBiometry.value,
                                renderSavedCards = renderSavedCards.value,
                                renderGooglePay = renderGooglePay.value
                            )

                        }
                    ) {
                        Text("Сгенерировать JWT и вставить в поле ввода")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 40.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isRenderSecurityCvv = renderSecurityCvv.value
                            isRenderSecurityBiometry = renderSecurityBiometry.value
                            isRenderSavedCards = renderSavedCards.value
                            isRenderGooglePay = renderGooglePay.value

                            if (tokenText.value.text.isEmpty()) {
                                Toast.makeText(context, "Добавьте JWT в поле ввода", Toast.LENGTH_LONG).show()
                            } else {
                                testInitSdk(this@TestActivity)

                                AirbaPaySdk.authJwt(
                                    jwt = tokenText.value.text,
                                    onError = {},
                                    onSuccess = {
                                        AirbaPaySdk.standardFlow(
                                            context = this@TestActivity,
                                            isGooglePayNative = nativeGooglePay.value
                                        )
                                    }
                                )
                            }
                        }
                    ) {
                        Text("Стандартный флоу JWT")
                    }


                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            if (tokenText.value.text.isEmpty()) {
                                Toast.makeText(context, "Добавьте JWT в поле ввода", Toast.LENGTH_LONG).show()
                            } else {
                                testInitSdk(this@TestActivity)

                                val intent = Intent(
                                    this@TestActivity,
                                    TestGooglePayExternalActivity::class.java
                                )
                                intent.putExtra("jwt", tokenText.value.text)
                                startActivity(intent)
                            }
                        }
                    ) {
                        Text("Тест внешнего API GooglePay JWT")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            if (tokenText.value.text.isEmpty()) {
                                Toast.makeText(context, "Добавьте JWT в поле ввода", Toast.LENGTH_LONG).show()
                            } else {
                                isRenderSecurityCvv = renderSecurityCvv.value
                                isRenderSecurityBiometry = renderSecurityBiometry.value

                                testInitSdk(this@TestActivity)
                                val intent = Intent(
                                    this@TestActivity,
                                    TestCardsExternalActivity::class.java
                                )
                                intent.putExtra("jwt", tokenText.value.text)
                                startActivity(intent)
                            }
                        }
                    ) {
                        Text("Тест внешнего API сохраненных карт JWT")
                    }


                    Text(
                        text = "Настройки только для Стандартного флоу ",
                        modifier = Modifier.padding(10.dp).padding(top = 30.dp)
                    )

                    DropdownList(
                        title1 = "CVV - NULL",
                        title2 = "CVV - FALSE",
                        title3 = "CVV - TRUE",
                        showDropdown = showDropdownRenderSecurityCvv,
                        isRender = renderSecurityCvv
                    )
                    DropdownList(
                        title1 = "Биометрия - NULL",
                        title2 = "Биометрия - FALSE",
                        title3 = "Биометрия - TRUE",
                        showDropdown = showDropdownRenderSecurityBiometry,
                        isRender = renderSecurityBiometry
                    )
                    DropdownList(
                        title1 = "Сохраненные карты - NULL",
                        title2 = "Сохраненные карты - FALSE",
                        title3 = "Сохраненные карты - TRUE",
                        showDropdown = showDropdownRenderSavedCards,
                        isRender = renderSavedCards
                    )
                    DropdownList(
                        title1 = "GooglePay - NULL",
                        title2 = "GooglePay - FALSE",
                        title3 = "GooglePay - TRUE",
                        showDropdown = showDropdownRenderGooglePay,
                        isRender = renderGooglePay
                    )

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
    autoCharge: Int = 0,
    invoiceId: String = Date().time.toString(),
    isLoading: MutableState<Boolean>,
    onSuccess: (String) -> Unit,
    renderSecurityCvv: Boolean? = null,
    renderSecurityBiometry: Boolean? = null,
    renderGooglePay: Boolean? = null,
    renderSavedCards: Boolean? = null
) {
    isLoading.value = true
    AirbaPaySdk.authPassword(
        onSuccess = { token ->
            val someOrderNumber = Date().time

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
                accountId = "77061111112",
                onSuccess = { result ->
                    onSuccess(result.token ?: "")
                },
                onError = {
                    isLoading.value = false
                    Toast.makeText(this, "Что-то пошло не так", Toast.LENGTH_SHORT).show()
                },
                failureCallback = "https://site.kz/failure-clb",
                successCallback = "https://site.kz/success-clb",
                autoCharge = autoCharge,
                purchaseAmount = 1500.45,
                invoiceId = invoiceId,
                orderNumber = someOrderNumber.toString(),
                renderSecurityBiometry = renderSecurityBiometry,
                renderSecurityCvv = renderSecurityCvv,
                renderGooglePay = renderGooglePay,
                renderSavedCards = renderSavedCards
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

private fun testInitSdk(
    activity: Activity,
    needDisableScreenShot: Boolean = false
) {
    DataHolder.hasSavedCards = false

    AirbaPaySdk.initSdk(
        enabledLogsForProd = false, //todo этот же параметр отвечает за отправку логов в дебаг сборке, если == true
        context = activity,
        isProd = false, //true, //
        phone = "77061111112",//"77051111117",
        lang = AirbaPaySdk.Lang.RU,
        userEmail = "test@test.com",
        colorBrandMain = Color(0xFFFC6B3F),
        actionOnCloseProcessing = { _activity, paymentSubmittingResult ->
            if (paymentSubmittingResult) {
                Log.e("AirbaPaySdk", "initProcessing success");
            } else {
                Log.e("AirbaPaySdk", "initProcessing error");
            }
            _activity.startActivity(Intent(_activity, TestActivity::class.java))
            _activity.finish()
        },
        needDisableScreenShot = needDisableScreenShot
//        openCustomPageSuccess = {  context.startActivity(Intent(context, CustomSuccessActivity::java.class)) },

    )
}

@Composable
private fun DropdownList(
    title1: String,
    title2: String,
    title3: String,
    showDropdown: MutableState<Boolean>,
    isRender: MutableState<Boolean?>
) {
    Button(
        modifier = Modifier
            .padding(top = 20.dp, bottom = 20.dp)
            .fillMaxWidth()
            .padding(horizontal = 50.dp),
        onClick = {
            showDropdown.value = !showDropdown.value
        },
        content = {
            val title = when (isRender.value) {
                null -> title1
                false -> title2
                else -> title3
            }

            Text(
                text = title,
                modifier = Modifier.padding(3.dp)
            )
        }
    )

    if (showDropdown.value) {
        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .background(ColorsSdk.stateSuccess)
                .clickable {
                    isRender.value = null
                    showDropdown.value = false
                },
            content = {
                Text(title1)
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .background(ColorsSdk.stateSuccess)
                .clickable {
                    isRender.value = false
                    showDropdown.value = false
                },
            content = {
                Text(title2)
            }
        )

        Box(
            modifier = Modifier
                .padding(top = 5.dp, bottom = 20.dp)
                .fillMaxWidth()
                .padding(horizontal = 50.dp)
                .background(ColorsSdk.stateSuccess)
                .clickable {
                    isRender.value = true
                    showDropdown.value = false
                },
            content = {
                Text(title3)
            }
        )

    }
}
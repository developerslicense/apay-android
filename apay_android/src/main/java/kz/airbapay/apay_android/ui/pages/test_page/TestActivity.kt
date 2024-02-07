package kz.airbapay.apay_android.ui.pages.test_page

import android.content.Intent
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.model.AuthRequest
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.TestAirbaPayStates
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.startAirbaPay
import kz.airbapay.apay_android.ui.pages.home.presentation.SwitchedView
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import java.util.Date

class TestActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            val scrollState = rememberScrollState()
            val autoCharge = remember { mutableStateOf(false) }
            val featureGooglePay = remember { mutableStateOf(true) }
            val featureSavedCards = remember { mutableStateOf(true) }
            val isLoading = remember { mutableStateOf(false) }

            ConstraintLayout {


                Column(
                    verticalArrangement = Arrangement.Top,
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                ) {
                    Text(
                        text = "Тестовые карты \n 4111 1111 1111 1616 cvv 333 \n 4111 1111 1111 1111 cvv 123",
                        modifier = Modifier.padding(16.dp)
                    )

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            TestAirbaPayStates.shutDownTestFeatureGooglePay = !featureGooglePay.value
                            TestAirbaPayStates.shutDownTestFeatureSavedCards = !featureSavedCards.value

                            AirbaPaySdk.initOnCreate(
                                context = this@TestActivity.application,
                                isProd = false,
                                accountId = "77051111111",
                                phone = "77051111117",
                                shopId = "test-merchant",
                                lang = AirbaPaySdk.Lang.RU,
                                password = "123456",
                                terminalId = "64216e7ccc4a48db060dd689",
                                failureCallback = "https://site.kz/failure-clb",
                                successCallback = "https://site.kz/success-clb",
                                userEmail = "test@test.com",
                                colorBrandMain = Color(0xFFFC6B3F),
                                autoCharge = if (autoCharge.value) 1 else 0
                            )
                            initProcessing()
                            startAirbaPay(
                                activity = this@TestActivity,
                                redirectToCustomSuccessPage = null//{
//                                    activity.startActivity(Intent(activity, CustomSuccessActivity::java.class))
//                                    activity.finish()
                                //  }
                            )
                        }
                    ) {
                        Text("Переход на эквайринг")
                    }

                    Button(
                        modifier = Modifier
                            .padding(top = 20.dp, bottom = 20.dp)
                            .fillMaxWidth()
                            .padding(horizontal = 50.dp),
                        onClick = {
                            isLoading.value = true
                            val authRequest = AuthRequest(
                                paymentId = null,
                                password = DataHolder.password,
                                terminalId = DataHolder.terminalId,
                                user = DataHolder.shopId
                            )
                            Repository.initRepositories(this@TestActivity)

                            Repository.authRepository?.auth(
                                param = authRequest,
                                result = {
                                    DataHolder.accessToken = it.accessToken

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
                                error = { isLoading.value = false }
                            )

                        }
                    )
                    {
                        Text("Удалить привязанные карты")
                    }

                    SwitchedView("AutoCharge 0 (off) / 1 (on)", autoCharge)
                    SwitchedView("Feature GooglePay", featureGooglePay)
                    SwitchedView("Feature Saved cards", featureSavedCards)
                }

                if (isLoading.value) {
                    ProgressBarView()
                }
            }
        }
    }
}

private fun initProcessing() {
    val someInvoiceId = Date().time
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
            amount = 1000,
            companyId = "210840019439"
        ),
        AirbaPaySdk.SettlementPayment(
            amount = 500,
            companyId = "254353"
        )
    )

    AirbaPaySdk.initProcessing(
        purchaseAmount = 1500,
        invoiceId = someInvoiceId.toString(),
        orderNumber = someOrderNumber.toString(),
        goods = goods,
        settlementPayments = settlementPayment,
        onProcessingResult = { activity, result ->
            if (result) {
                Log.e("AirbaPaySdk", "initProcessing success");
            } else {
                Log.e("AirbaPaySdk", "initProcessing error");
            }
            activity.startActivity(Intent(activity, TestActivity::class.java))
        }
    )

}

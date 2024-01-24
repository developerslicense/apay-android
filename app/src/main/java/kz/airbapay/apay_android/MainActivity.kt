package kz.airbapay.apay_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.theme.Apay_androidTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AirbaPaySdk.initOnCreate(
            context = this.application,
            isProd = false,
            accountId = "77051111118",
            phone = "77051111117",
            shopId = "test-merchant",
            lang = AirbaPaySdk.Lang.RU,
            password = "123456",
            terminalId = "64216e7ccc4a48db060dd689",
            failureCallback = "https://site.kz/failure-clb",
            successCallback = "https://site.kz/success-clb",
            userEmail = "test@test.com",
            colorBrandMain = Color(0xFFFC6B3F)
        )

        setContent {

            Apay_androidTheme {
                Column(
                    verticalArrangement = Arrangement.Center,
                    modifier = Modifier.fillMaxSize()
                ) {
                    Text(
                        text = "Для теста сохраненной карты с вводом CVV\n 4111 1111 1111 1616 cvv 333",
                        modifier = Modifier.padding(16.dp)
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Button(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 50.dp, horizontal = 50.dp),
                        onClick = {
                            initProcessing()
                            startAirbaPay(
                                activity = this@MainActivity,
                                redirectToCustomSuccessPage = null//{
//                                    activity.startActivity(Intent(activity, CustomSuccessActivity::java.class))
//                                    activity.finish()
                              //  }
                            )
                        }
                    ) {
                        Text("переход на эквайринг")
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
                startActivity(Intent(activity, MainActivity::class.java))
            }
        )
    }
}

package kz.airbapay.apay_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.theme.Apay_androidTheme
import java.util.Date

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            Apay_androidTheme {
                // A surface container using the 'background' color from the theme
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    Column(
                        verticalArrangement = Arrangement.Center
                    ) {
                        Button(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 50.dp, horizontal = 50.dp),
                            onClick = {
                                val timeStamp = Date().time

                                val goods = listOf(
                                    AirbaPaySdk.Goods(
                                        model = "Чай Tess Banana Split черный 20 пирамидок",
                                        brand = "Tess",
                                        category = "Черный чай",
                                        quantity = 1,
                                        price = 1000
                                    )
                                )

                                val settlementPayment = listOf(
                                    AirbaPaySdk.SettlementPayment(
                                        1000,
                                        "test_id"
                                    )
                                )

                                AirbaPaySdk.startProcessing(
                                    context = this@MainActivity,
                                    isProd = false,
                                    purchaseAmount = 50150,
                                    phone = "77051234567",
                                    invoiceId = timeStamp.toString(),
                                    orderNumber = timeStamp.toString(),
                                    shopId = "test-merchant",
                                    lang = AirbaPaySdk.Lang.RU,
                                    password = "123456",
                                    terminalId = "64216e7ccc4a48db060dd689",
                                    failureBackUrl = "https://site.kz/failure",
                                    failureCallback = "https://site.kz/failure-clb",
                                    successBackUrl = "https://site.kz/success",
                                    successCallback = "https://site.kz/success-clb",
                                    userEmail = "test@test.com",
                                    goods = goods,
                                    settlementPayments = settlementPayment // emptyList()
//                                    colorBrandMain = Color.Red
                                )
                            }
                        ) {
                            Text("переход на эквайринг")
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun Greeting(name: String, modifier: Modifier = Modifier) {
    Text(
        text = "Hello $name!",
        modifier = modifier
    )
}

@Preview(showBackground = true)
@Composable
fun GreetingPreview() {
    Apay_androidTheme {
        Greeting("Android")
    }
}
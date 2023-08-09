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
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.theme.Apay_androidTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    private val isBottomSheet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AirbaPaySdk.initOnCreate(
            isProd = false,
            phone = "77051234567",
            shopId = "test-merchant",
            lang = AirbaPaySdk.Lang.RU,
            password = "123456",
            terminalId = "64216e7ccc4a48db060dd689",
            failureCallback = "https://site.kz/failure-clb",
            successCallback = "https://site.kz/success-clb",
            needShowSdkSuccessPage = true,
            userEmail = "test@test.com",
            // colorBrandMain = Color.Red
        )

        if (!isBottomSheet) {
            initProcessing()
        }

        setContent {

            Apay_androidTheme {
                if (isBottomSheet) {
                    AirbaPaySdkModalBottomSheetProcessingCompose(
                        content = { actionShowBottomSheet ->
                            PageContentForBottomSheet(actionShowBottomSheet)
                        }
                    )

                } else {
                    PageContentForView()
                }
            }
        }
    }

    @Composable
    private fun PageContentForBottomSheet(
        actionShowBottomSheet: () -> Unit
    ) {
        Column(
            verticalArrangement = Arrangement.Center,
            modifier = Modifier.fillMaxSize()
        ) {
            Button(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 50.dp, horizontal = 50.dp),
                onClick = {
                    initProcessing()
                    actionShowBottomSheet()
                }
            ) {
                Text("переход на эквайринг")
            }
        }
    }

    @OptIn(ExperimentalMaterial3Api::class)
    @Composable
    private fun PageContentForView() {
        Column(
            verticalArrangement = Arrangement.Top,
            modifier = Modifier.fillMaxSize()
        ) {
            TopAppBar(
                title = { Text("Оплата заказа") }
            )

            AirbaPaySdkModalProcessingCompose()
        }
    }

    private fun initProcessing() {
        val id = Date().time

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
                amount = 1000,
                companyId = "test_id"
            )
        )

        AirbaPaySdk.initProcessing(
            purchaseAmount = 50150,
            invoiceId = id.toString(),
            orderNumber = id.toString(),
            goods = goods,
            settlementPayments = emptyList(), //settlementPayment
        )
    }
}

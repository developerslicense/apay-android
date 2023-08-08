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
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.theme.Apay_androidTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            val isProcessingInited = rememberSaveable {
                mutableStateOf(false)
            }

            Apay_androidTheme {
                AirbaPaySdkModalBottomSheetProcessingCompose(
                    isProcessingInited = isProcessingInited.value,
                    content = { actionShowBottomSheet ->
                        PageContent(
                            actionShowBottomSheet = actionShowBottomSheet,
                            isSdkInited = isProcessingInited
                        )
                    }
                )
            }
        }
    }

    @Composable
    private fun PageContent(
        actionShowBottomSheet: () -> Unit,
        isSdkInited: MutableState<Boolean>
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
                    isSdkInited.value = true
                    actionShowBottomSheet()
                }
            ) {
                Text("переход на эквайринг")
            }
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
                1000,
                "test_id"
            )
        )

        AirbaPaySdk.initProcessing(
            isProd = false,
            purchaseAmount = id, //50150,
            phone = "77051234567",
            invoiceId = id.toString(),
            orderNumber = id.toString(),
            shopId = "test-merchant",
            lang = AirbaPaySdk.Lang.RU,
            password = "123456",
            terminalId = "64216e7ccc4a48db060dd689",
            failureCallback = "https://site.kz/failure-clb",
            successCallback = "https://site.kz/success-clb",
            needShowSdkSuccessPage = true,
            userEmail = "test@test.com",
            goods = goods,
            settlementPayments = emptyList(), //settlementPayment
    //                        colorBrandMain = Color.Red
        )
    }
}

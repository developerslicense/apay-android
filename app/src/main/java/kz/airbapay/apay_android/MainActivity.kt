package kz.airbapay.apay_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.ui.theme.Apay_androidTheme
import java.util.Date

class MainActivity : ComponentActivity() {

    private val isBottomSheet = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        AirbaPaySdk.initOnCreate(
            isProd = false,
            phone = "77051111111",
            shopId = "test-merchant",
            lang = AirbaPaySdk.Lang.RU,
            password = "123456",
            terminalId = "64216e7ccc4a48db060dd689",
            failureCallback = "https://site.kz/failure-clb",
            successCallback = "https://site.kz/success-clb",
            needShowSdkSuccessPage = true,
            userEmail = "test@test.com",
//            colorBrandMain = Color.Red
        )

        if (!isBottomSheet) {
            initProcessing()
        }

        setContent {

            Apay_androidTheme {
                if (isBottomSheet) {
                    AirbaPaySdkProcessingBottomSheet(
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
        ConstraintLayout(
            modifier = Modifier.fillMaxSize()
        ) {
            val isLoading = remember { mutableStateOf(true) }

            Column(
                verticalArrangement = Arrangement.Top,
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
            ) {
                TopAppBar(
                    title = { Text("Оплата заказа") }
                )

                AirbaPaySdkProcessingView(
                    actionOnLoadingCompleted = { isLoading.value = false },
                    needShowProgressBar = false,
                    backgroundColor = Color.White
                )
            }

            val (progressRef) = createRefs()

            if (isLoading.value) {
                CircularProgressIndicator(
                    color = Color.Blue,
                    modifier = Modifier
                        .fillMaxWidth(0.3f)
                        .constrainAs(progressRef) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start)
                            end.linkTo(parent.end)
                        }
                )
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
            invoiceId = someInvoiceId.toString(),
            orderNumber = someOrderNumber.toString(),
            goods = goods,
            settlementPayments = emptyList(), //settlementPayment
        )
    }
}

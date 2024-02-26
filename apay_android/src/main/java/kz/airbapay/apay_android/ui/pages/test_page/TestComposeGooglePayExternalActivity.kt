package kz.airbapay.apay_android.ui.pages.test_page

import android.app.Activity
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.BaseComposeGooglePayActivity
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GooglePayNativeCompose
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

internal class TestComposeGooglePayExternalActivity: BaseComposeGooglePayActivity() {

    private val isLoading = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initOnCreate(this@TestComposeGooglePayExternalActivity)

        authGooglePay(
            onSuccess = {
                isLoading.value = false
                // needShowGooglePayButton = true
            },
            onFailed = {
                isLoading.value = false
                // needShowGooglePayButton = false
            }
        )

        setContent {
            ConstraintLayout {
                GooglePayNativeCompose(
                    Modifier
                        .fillMaxWidth()
                        .padding(top = 20.dp)
                        .padding(horizontal = 16.dp)
                )

                if (isLoading.value) {
                    ProgressBarView()
                }
            }
        }
    }
}

private fun initOnCreate(
    activity: Activity
) {
    AirbaPaySdk.initOnCreate(
        context = activity,
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
        autoCharge = 0,
        hideInternalGooglePayButton = true
    )
}

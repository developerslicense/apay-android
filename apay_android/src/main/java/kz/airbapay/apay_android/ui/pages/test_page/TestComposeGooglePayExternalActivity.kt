package kz.airbapay.apay_android.ui.pages.test_page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayGooglePayNativeView
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

internal class TestComposeGooglePayExternalActivity: ComponentActivity() {

    private val isLoading = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestSdk(this@TestComposeGooglePayExternalActivity)
        val airbaPay = AirbaPayBaseGooglePay(this)

        airbaPay.authGooglePay(
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
                AirbaPayGooglePayNativeView(
                    airbaPayBaseGooglePay = airbaPay,
                    buttonTheme = 1,
                    buttonType = 8,
                    cornerRadius = 8,
                    modifier = Modifier
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



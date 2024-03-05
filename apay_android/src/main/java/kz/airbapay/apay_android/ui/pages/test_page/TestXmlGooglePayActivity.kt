package kz.airbapay.apay_android.ui.pages.test_page

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.ui.platform.ComposeView
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayGooglePayNativeView

internal class TestXmlGooglePayActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_xml_google_pay)

        val airbaPay = AirbaPayBaseGooglePay(this)
        findViewById<ComposeView>(R.id.googlePay)?.apply {
            setContent {
                AirbaPayGooglePayNativeView(
                    airbaPayBaseGooglePay = airbaPay,
                    buttonTheme = 1,
                    buttonType = 8,
                    cornerRadius = 8
                )
            }
        }

        initTestSdk(this)

        airbaPay.authGooglePay(
            onSuccess = {
//                isLoading.value = false
                // needShowGooglePayButton = true
            },
            onFailed = {
//                isLoading.value = false
                // needShowGooglePayButton = false
            }
        )
    }

}
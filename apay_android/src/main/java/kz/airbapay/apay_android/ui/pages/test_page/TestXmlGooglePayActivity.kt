package kz.airbapay.apay_android.ui.pages.test_page

import android.os.Bundle
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.BaseXmlGooglePayActivity

internal class TestXmlGooglePayActivity : BaseXmlGooglePayActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_test_xml_google_pay)

        initTestSdk(this)

        authGooglePay(
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
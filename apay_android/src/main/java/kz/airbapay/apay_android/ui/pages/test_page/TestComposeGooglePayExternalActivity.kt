package kz.airbapay.apay_android.ui.pages.test_page

import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.BaseComposeGooglePayActivity
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.GooglePayNativeCompose
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

internal class TestComposeGooglePayExternalActivity: BaseComposeGooglePayActivity() {

    private val isLoading = mutableStateOf(true)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initTestSdk(this@TestComposeGooglePayExternalActivity)

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



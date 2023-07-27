package kz.airbapay.apay_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kz.airbapay.apay_android.ui.pages.success.SuccessPage

class AirbaPayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = intent.getStringExtra("airba_pay_args")
        Log.i("Arguments for AirbaPay", arguments.orEmpty())

        setContent {
            SuccessPage()
        }
    }
}
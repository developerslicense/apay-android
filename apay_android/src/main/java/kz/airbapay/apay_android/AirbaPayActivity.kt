package kz.airbapay.apay_android

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity

class AirbaPayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val arguments = intent.getStringExtra("airba_pay_args")
        Log.i("Arguments for AirbaPay", arguments.orEmpty())


    }
}
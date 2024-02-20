package kz.airbapay.apay_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import kz.airbapay.apay_android.ui.pages.test_page.TestActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}


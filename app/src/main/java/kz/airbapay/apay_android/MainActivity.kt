package kz.airbapay.apay_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import kz.airbapay.apay_android.ui.pages.card_reader2.ScannerActivity

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val intent = Intent(this, ScannerActivity::class.java)
//        val intent = Intent(this, TestActivity::class.java)
        startActivity(intent)
        finishAffinity()
    }
}

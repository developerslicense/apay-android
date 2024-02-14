package kz.airbapay.apay_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import io.card.payment.CardIOActivity

class MainActivity : ComponentActivity() {
    val SCAN_REQUEST_CODE = 420

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = Intent(this, TestActivity::class.java)
//        startActivity(intent)
//        finishAffinity()
        startCardScanner()
    }

    fun startCardScanner() {
        val scanIntent = Intent(this, CardIOActivity::class.java)

        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, true)
        scanIntent.putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON, false)
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY, true)
        scanIntent.putExtra(CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION, true)
        scanIntent.putExtra(CardIOActivity.EXTRA_HIDE_CARDIO_LOGO, true)
        scanIntent.putExtra(CardIOActivity.EXTRA_KEEP_APPLICATION_THEME, true)

        startActivityForResult(
            scanIntent,
            SCAN_REQUEST_CODE,
            null
        )
    }
}

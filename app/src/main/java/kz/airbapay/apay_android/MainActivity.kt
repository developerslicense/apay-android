package kz.airbapay.apay_android

import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import io.card.payment.CardIOActivity
import io.card.payment.CardScannerWrapper
import io.card.payment.CreditCard

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
        CardScannerWrapper.initCardScanner()

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

  /*  @Throws(Exception::class)
    fun setFinalStatic() {
//        field.isAccessible = true
        val modifiersField = Field::class.java.getDeclaredField("mMainLayout")
        modifiersField.isAccessible = true
//        modifiersField.setInt(field, field.modifiers and Modifier.FINAL.inv())
//        val fl = FrameLayout(this)
//        val reflectFL = modifiersField.get(fl)
//        reflectFL.

        val myWebView = TextView(this)
        val myValueToSet = ...;
        val webViewClass = TextView::class.java
        val field = webViewClass.getField("someField")
        field.isAccessible = true
        field.set(myWebView, myValueToSet);
    }*/

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (
            requestCode == SCAN_REQUEST_CODE &&
            data != null &&
            data.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT)
        ) {
            val scanResult: CreditCard? =
                data.getParcelableExtra(CardIOActivity.EXTRA_SCAN_RESULT)

            Log.v("Log", "aaaaaaaaaaa" +scanResult?.formattedCardNumber ?: "")
        }
    }
}

fun CardIOActivity.addBottomSheetCancel() {

}

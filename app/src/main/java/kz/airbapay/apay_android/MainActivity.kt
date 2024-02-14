package kz.airbapay.apay_android

import android.content.Intent
import android.graphics.Color
import android.os.Bundle
import androidx.activity.ComponentActivity
import kz.airbapay.apay_android.ui.pages.card_reader2.DocumentScannerActivity
import kz.airbapay.apay_android.ui.pages.card_reader2.constants.DefaultSetting
import kz.airbapay.apay_android.ui.pages.card_reader2.constants.DocumentScannerExtra
import kz.airbapay.apay_android.ui.pages.card_reader3.io.card.payment.CardIOActivity
import kz.airbapay.apay_android.ui.pages.card_reader3.io.card.payment.i18n.locales.LocalizedStringsRU

class MainActivity : ComponentActivity() {
    //todo
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = Intent(this, ScannerActivity::class.java)
//        val intent = Intent(this, TestActivity::class.java)
//        val intent = createDocumentScanIntent()
//        startActivity(intent)
//        finishAffinity()
        onScan()
    }

    fun onScan() {
        val intent: Intent = Intent(this, CardIOActivity::class.java)
            .putExtra(CardIOActivity.EXTRA_NO_CAMERA, false)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_EXPIRY, false)
            .putExtra(CardIOActivity.EXTRA_SCAN_EXPIRY, false)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_CVV, false)
            .putExtra(CardIOActivity.EXTRA_REQUIRE_POSTAL_CODE, false)
            .putExtra(
                CardIOActivity.EXTRA_RESTRICT_POSTAL_CODE_TO_NUMERIC_ONLY,
                false
            )
            .putExtra(
                CardIOActivity.EXTRA_REQUIRE_CARDHOLDER_NAME,
                false
            )
            .putExtra(
                CardIOActivity.EXTRA_SUPPRESS_MANUAL_ENTRY,
                true
            )
            .putExtra(CardIOActivity.EXTRA_USE_CARDIO_LOGO, false)
            .putExtra(
                CardIOActivity.EXTRA_LANGUAGE_OR_LOCALE,
                LocalizedStringsRU().name
            )
            .putExtra(
                CardIOActivity.EXTRA_USE_PAYPAL_ACTIONBAR_ICON,
                false
            )
            .putExtra(
                CardIOActivity.EXTRA_KEEP_APPLICATION_THEME,
                false
            )
            .putExtra(CardIOActivity.EXTRA_GUIDE_COLOR, Color.GREEN)
            .putExtra(
                CardIOActivity.EXTRA_SUPPRESS_CONFIRMATION,
                false
            )
            .putExtra(CardIOActivity.EXTRA_SUPPRESS_SCAN, false)
            .putExtra(CardIOActivity.EXTRA_RETURN_CARD_IMAGE, false)

        startActivityForResult(
            intent,
            100
        )
    }


    private fun createDocumentScanIntent(): Intent {
        val documentScanIntent = Intent(this, DocumentScannerActivity::class.java)
        documentScanIntent.putExtra(
            DocumentScannerExtra.EXTRA_CROPPED_IMAGE_QUALITY,
            DefaultSetting.CROPPED_IMAGE_QUALITY
        )

        return documentScanIntent
    }
}

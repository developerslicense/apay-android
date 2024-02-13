package kz.airbapay.apay_android

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import kz.airbapay.apay_android.ui.pages.card_reader2.DocumentScannerActivity
import kz.airbapay.apay_android.ui.pages.card_reader2.constants.DefaultSetting
import kz.airbapay.apay_android.ui.pages.card_reader2.constants.DocumentScannerExtra

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        val intent = Intent(this, ScannerActivity::class.java)
//        val intent = Intent(this, TestActivity::class.java)
        val intent = createDocumentScanIntent()
        startActivity(intent)
        finishAffinity()


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

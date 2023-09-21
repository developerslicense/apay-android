package kz.airbapay.apay_android.ui.pages.card_reader

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.card_reader.bl.ScanBaseActivity

internal class ScanActivityImpl : ScanBaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.irdcs_activity_scan_card)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (checkSelfPermission(Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(arrayOf(Manifest.permission.CAMERA), 110)
            } else {
                mIsPermissionCheckDone = true
            }
        } else {
            // no permission checks
            mIsPermissionCheckDone = true
        }
        setViewIds(R.id.cardRectangle, R.id.shadedBackground, R.id.texture)
    }

    override fun onCardScanned(numberResult: String?) {
        println("aaaaa $numberResult")
        val intent = Intent()
        intent.putExtra(RESULT_CARD_NUMBER, numberResult)
        setResult(RESULT_OK, intent)
        finish()
    }

    companion object {
        const val RESULT_CARD_NUMBER = "cardNumber"
    }
}
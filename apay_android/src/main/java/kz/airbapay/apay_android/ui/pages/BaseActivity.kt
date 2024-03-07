package kz.airbapay.apay_android.ui.pages

import androidx.activity.ComponentActivity
import kz.airbapay.apay_android.network.loggly.LoggerHelper
import kz.airbapay.apay_android.network.loggly.LoggerObserver

internal abstract class BaseActivity: ComponentActivity(), LoggerObserver {

    override fun onResume() {
        super.onResume()
        LoggerHelper.addObserver(this)
    }

    override fun onPause() {
        super.onPause()
        LoggerHelper.removeObserver(this)
    }
}
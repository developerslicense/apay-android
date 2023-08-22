package kz.airbapay.apay_android.data.utils

import kz.airbapay.apay_android.AirbaPaySdk

internal fun getStrFromRes(ru: String, kz: String) = if (DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang) {
    kz
} else {
    ru
}


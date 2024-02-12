package kz.airbapay.apay_android.data.utils

import android.content.Context
import android.hardware.fingerprint.FingerprintManager
import android.os.Build


internal fun checkIsDeviceSecure(
    context: Context
): Boolean {
   return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
            val fingerprintManager = context.getSystemService(Context.FINGERPRINT_SERVICE) as FingerprintManager

            return fingerprintManager.hasEnrolledFingerprints()
        } else {
            false
        }

//    val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
//    val isDeviceSecure = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
//        keyguardManager.isDeviceSecure // нет разделения на фингерпринт и пинкод. проблема в том,
//        что если не установлен фингерпринт, то неправильно отрабатывает для гуглпэя
//    } else {
//        false
//    }
//    return isDeviceSecure
}
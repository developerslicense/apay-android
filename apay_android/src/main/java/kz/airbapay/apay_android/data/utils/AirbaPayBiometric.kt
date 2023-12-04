package kz.airbapay.apay_android.data.utils

import android.app.Activity
import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.widget.Toast
import kz.airbapay.apay_android.data.constant.accessToCardRestricted
import kz.airbapay.apay_android.data.constant.authenticateFingerprint
import kz.airbapay.apay_android.data.constant.requestAccessToSavedCards
import kz.airbapay.apay_android.data.constant.textCancel

internal class AirbaPayBiometric(
    private val context: Context
) {
    private var cancellationSignal: CancellationSignal? = null

    fun authenticate(
        onSuccess: () -> Unit,
        onError: () -> Unit,
    ) {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (keyguardManager.isKeyguardSecure) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                val biometricPrompt = BiometricPrompt.Builder(context)
                    .setTitle(requestAccessToSavedCards()) //"Проверка личности"
                    .setDescription(authenticateFingerprint())
                    .setNegativeButton(textCancel(), context.mainExecutor) { dialog, which ->
                        onError()
//                        notifyUser(accessToCardRestricted())
                    }.build()

                biometricPrompt.authenticate(
                    getCancellationSignal(onError),
                    context.mainExecutor,
                    object : BiometricPrompt.AuthenticationCallback() {
                        override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                            super.onAuthenticationError(errorCode, errString)
                            notifyUser(accessToCardRestricted())
                            onError()
                        }

                        override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                            super.onAuthenticationSucceeded(result)
                            onSuccess()
                        }
                    }
                )

            } else {
                val createConfirmDeviceCredentialIntent =
                    keyguardManager.createConfirmDeviceCredentialIntent(null, null)
                if (createConfirmDeviceCredentialIntent != null) {
                    try {
                        (context as Activity).startActivityForResult(
                            createConfirmDeviceCredentialIntent,
                            1
                        )
                    } catch (e: ActivityNotFoundException) {
//                        e.printStackTrace()
                    }
                }
            }
        } else {
//            notifyUser(accessToCardRestricted())
            onError()
        }
    }

    private fun getCancellationSignal(
        onError: () -> Unit
    ): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            onError()
        }
        return cancellationSignal as CancellationSignal
    }

    private fun notifyUser(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
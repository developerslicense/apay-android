package kz.airbapay.apay_android.data.utils

import android.annotation.SuppressLint
import android.app.Activity
import android.app.KeyguardManager
import android.content.ActivityNotFoundException
import android.content.Context
import android.content.pm.PackageManager
import android.hardware.biometrics.BiometricPrompt
import android.os.Build
import android.os.CancellationSignal
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.core.app.ActivityCompat

class AirbaPayBiometric(
    private val context: Context
) {
    private var cancellationSignal: CancellationSignal? = null
    private val authenticationCallback: BiometricPrompt.AuthenticationCallback
        get() =
            @RequiresApi(Build.VERSION_CODES.P)
            object : BiometricPrompt.AuthenticationCallback() {
                override fun onAuthenticationError(errorCode: Int, errString: CharSequence?) {
                    super.onAuthenticationError(errorCode, errString)
                    notifyUser("Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult?) {
                    super.onAuthenticationSucceeded(result)
                    notifyUser("Authentication Success!")
//                    startActivity(Intent(this@MainActivity, Secret::class.java))
                }
            }

    fun authenticate() {
        val keyguardManager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

        if (keyguardManager.isKeyguardSecure) {
            if (isAndroidVersionCompareCondition()) {
                val biometricPrompt = BiometricPrompt.Builder(context)
                    .setTitle("Проверка личности")
                    .setDescription("Отсканируйте отпечаток пальца")
                    .setNegativeButton("Отменить", context.mainExecutor) { dialog, which ->
                    }.build()

                biometricPrompt.authenticate(
                    getCancellationSignal(),
                    context.mainExecutor,
                    authenticationCallback
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
                        e.printStackTrace()
                    }
                }
            }

        } else if (!keyguardManager.isKeyguardSecure) {
            notifyUser("Fingerprint hs not been enabled in settings.")

        } else if (ActivityCompat.checkSelfPermission(
                context,
                android.Manifest.permission.USE_BIOMETRIC
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            notifyUser("Fingerprint hs not been enabled in settings.")

        }
    }

    @SuppressLint("AnnotateVersionCheck")
    private fun isAndroidVersionCompareCondition() = Build.VERSION.SDK_INT >= Build.VERSION_CODES.P

    private fun getCancellationSignal(): CancellationSignal {
        cancellationSignal = CancellationSignal()
        cancellationSignal?.setOnCancelListener {
            notifyUser("Authentication was cancelled by the user")
        }
        return cancellationSignal as CancellationSignal
    }

    private fun notifyUser(message: String) {
        Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
    }
}
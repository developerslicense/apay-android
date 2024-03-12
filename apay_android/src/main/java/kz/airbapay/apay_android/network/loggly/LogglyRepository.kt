package kz.airbapay.apay_android.network.loggly

import androidx.annotation.WorkerThread
import kz.airbapay.apay_android.BuildConfig
import kz.airbapay.apay_android.network.api.LogglyApi
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.IOException

/**
 * Creates a Loggly client
 * @param token Loggly customer token
 * http://loggly.com/docs/customer-token-authentication-token/
 */

internal class LogglyRepository(
    private val loggly: LogglyApi
) {

    /**
     * Posts a log message to Loggly
     * @param message message to be logged
     */
    @WorkerThread
    @Throws(IOException::class)
    fun log(message: String): Boolean {
        return loggly.log(
            BuildConfig.LOGGLY_TOKEN,
            message.toRequestBody("application/json".toMediaTypeOrNull())
        ).execute().let{
            if(it.isSuccessful) {
//                println("loggly success $message")
                true
            } else {
//                println("loggly ${it.message()} ${it.errorBody()?.string().orEmpty()}")
                false
            }
        }
    }

}

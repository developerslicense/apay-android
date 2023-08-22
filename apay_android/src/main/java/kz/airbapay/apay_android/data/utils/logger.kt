package kz.airbapay.apay_android.data.utils

import android.util.Log
import retrofit2.Response

private const val TAG = "TagAirbaPay"

fun messageLog(message: String) {
    if (!DataHolder.isProd) {
        Log.i(TAG, message)
    }
}

fun <T> errorLog(e: Response<T>?) {
    if (!DataHolder.isProd) {
        if (e != null) {
            Log.i(TAG, "response error:")
            Log.i(TAG, "STATUS: ${e.code()}")
            Log.i(TAG, "BODY: ${e.body()}")
            Log.i(TAG, "ERROR_BODY: ${e.errorBody()}")
            Log.i(TAG, "HEADERS: ${e.headers()}")
            Log.i(TAG, "MESSAGE: ${e.message()}")
        } else {
            Log.i(TAG, "Error sending request!")
        }
    }
}

fun errorLog(e: Exception) {
    if (!DataHolder.isProd) {
        Log.i(TAG, "Error sending request!")
        Log.i(TAG, e.printStackTrace().toString())
    }
}

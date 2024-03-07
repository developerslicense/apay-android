package kz.airbapay.apay_android.network.loggly

import android.util.Log
import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.Repository

private const val TAG = "TagAirbaPay"
const val LOGGLY_API_URL = "https://logs-01.loggly.com/"

internal object Logger {

    private val gson = Gson()
    private val parentJob = Job()
    private val logScope = CoroutineScope(Dispatchers.IO + parentJob)

    fun log(
        message: String? = null,
        url: String? = null,
        method: String? = null,
        responseCode: String? = null,
        bodyParams: String? = null,
        response: String? = null
    ) {
        try {
            logScope.launch(Dispatchers.IO) {
                if (!DataHolder.isProd || DataHolder.enabledLogsForProd) {
                    Log.i(TAG, message ?: "")
                }

                if (DataHolder.isProd || DataHolder.enabledLogsForProd) {

                    val request = ApiLogEntry(
                        url = url,
                        method = method,
                        responseCode = responseCode,
                        bodyParams = bodyParams,
                        response = response,
                        message = message
                    )

                    val messages = gson.toJson(request)
                    Repository.logglyRepository?.log(messages)
                }
            }

        } catch (e: Exception) {
            e.printStackTrace()
            try {
                Repository.logglyRepository?.log("Crash loggly ${e.printStackTrace().toString().substring(0, 50)}")
            } catch (e: Exception) {
                Repository.logglyRepository?.log("Crash loggly =_=, no logs")
            }
        }
    }
}

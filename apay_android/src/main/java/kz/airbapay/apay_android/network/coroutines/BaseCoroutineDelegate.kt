package kz.airbapay.apay_android.network.coroutines

import com.google.gson.Gson
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.loggly.Logger
import retrofit2.Response

class BaseCoroutineDelegate : BaseCoroutine {
    private val gson = Gson()
    private val parentJob = Job()

    override val scope = CoroutineScope(Dispatchers.Main + parentJob)

    override fun clearCoroutine() {
        parentJob.cancelChildren()
    }

    override fun <T : Any> launch(
        paramsForLog: Any?,
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        error: (Response<T>?) -> Unit
    ) {

        if (DataHolder.baseUrl.isNotEmpty()) {
            scope.launch {
                requestFlow
                    .invoke()
                    .collect {
                        val response = (it as Response<T>)
                        if (response.isSuccessful) {
                            result(it)
                        } else {
                            error(it)
                        }
                        val params = gson.toJson(paramsForLog)

                        Logger.log(
                            url = response.raw().request.url.toString(),
                            responseCode = response.raw().code.toString(),
                            method = response.raw().request.method,
                            response = response.body()?.toString(),
                            bodyParams = params,
                            message = "launchRequest in delegate: " + response.message()
                        )
                    }
            }
        } else {
            println("Не выполнен initSdk")
            error(null)
        }
    }
}
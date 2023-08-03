package kz.airbapay.apay_android.network.coroutines

import androidx.compose.runtime.mutableStateOf
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancelChildren
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch
import retrofit2.Response

class BaseCoroutineDelegate : BaseCoroutine {
    private val parentJob = Job()

    override val scope = CoroutineScope(Dispatchers.Main + parentJob)
    override val hasInternet = mutableStateOf(true)

    override fun clearCoroutine() {
        parentJob.cancelChildren()
    }

    override fun <T : Any> launch(
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        error: (Response<T>) -> Unit
    ) {
        hasInternet.value = true

        scope.launch {
            requestFlow
                .invoke()
                .collect {
                    if ((it as Response<T>).isSuccessful) {
                        result(it)
                    } else {
                        error(it)
                    }
                }
        }
    }
}
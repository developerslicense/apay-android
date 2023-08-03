package kz.airbapay.apay_android.network.coroutines

import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface BaseCoroutine {

    val scope: CoroutineScope

    val hasInternet: MutableState<Boolean>

    /**
     * Очиска coroutine (если спользовать в паре с CoreLaunchViewModel
     * или CoreAndroidLaunchViewModel тогда отчистка происходит автоматически)
     */
    fun clearCoroutine()

    /**
     * Запуск холодного потока (flow) обработкой ошибки
     * @param requestFlow запрос в виде suspend функции
     * @param result результат
     * @param error блок ошибки
     * Для дополнительной информации https://kotlinlang.org/docs/reference/coroutines/flow.html
     */
    fun <T : Any> launch(
        requestFlow: suspend () -> Flow<T>,
        result: (T) -> Unit,
        error: (Response<T>) -> Unit
    )
}


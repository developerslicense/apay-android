package kz.airbapay.apay_android.network.base

import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

/**
 * Функция обработки http и прочих ошибок
 * @param call suspend функция
 */
suspend fun <T : Any> safeApiFlowCall(
    call: suspend () -> T
): Flow<T> = flow {
    emit(call.invoke())
}


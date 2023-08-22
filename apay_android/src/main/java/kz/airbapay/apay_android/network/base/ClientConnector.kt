package kz.airbapay.apay_android.network.base

import android.content.Context
import okhttp3.OkHttpClient
import retrofit2.Retrofit

internal class ClientConnector(
    context: Context
) {
    private val interceptor = BaseInterceptor(context)
    private val gson = provideGson()

    private val clientCoroutines: OkHttpClient = createOkHttpClientCoroutine(interceptor)

    val retrofit: Retrofit = provideRetrofit(clientCoroutines, gson)
}
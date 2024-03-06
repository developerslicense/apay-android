package kz.airbapay.apay_android.network.base

import android.content.Context
import kz.airbapay.apay_android.data.utils.DataHolder
import okhttp3.Interceptor
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.Protocol
import okhttp3.Response
import okhttp3.ResponseBody.Companion.toResponseBody
import java.io.IOException

internal class BaseInterceptor(
    val context: Context?
) : Interceptor {

    @Throws(IOException::class)
    override fun intercept(chain: Interceptor.Chain): Response {
        val result: Response
        val request = chain.request()
        val requestBuilder = request.newBuilder()

        if (context?.let { InternetConnectionUtils.isOnline(it) } == false) {
            return Response.Builder()
                .code(500)
                .body("{}".toResponseBody("application/json".toMediaTypeOrNull()))
                .protocol(Protocol.HTTP_2)
                .message("Нет подключения к интернету")
                .request(chain.request())
                .build()
        }

        requestBuilder.addHeader("Platform", "Android, 1.0.40")

        requestBuilder.addHeader(
            "Authorization",
            if(DataHolder.accessToken.isNullOrBlank()) "" else "Bearer ${DataHolder.accessToken}"
        )

        try {
            result = chain.proceed(requestBuilder.build())
        } catch (ex: IOException) {
            ex.printStackTrace()

            return Response.Builder()
                .code(500)
                .body("{}".toResponseBody("application/json".toMediaTypeOrNull()))
                .protocol(Protocol.HTTP_2)
                .message("Произошла непредвиденная ошибка")
                .request(chain.request())
                .build()

        }

        return result
    }

}

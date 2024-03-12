package kz.airbapay.apay_android.network.api

import kz.airbapay.apay_android.network.loggly.LOGGLY_API_URL
import kz.airbapay.apay_android.network.loggly.LogglyResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

/**
 * Loggly REST interface, used internally by Retrofit
 *
 * @author tony19@gmail.com
 * @since 1.0.3
 */
internal interface LogglyApi {
    /**
     * Posts a single log event to Loggly's REST endpoint
     * @param token Loggly customer token
     * @param tags CSV of tags
     * @param message log event to be posted
     * @return result of the post as a [com.github.tony19.loggly.LogglyResponse]
     */
    @Headers("Content-type: application/json; charset=utf-8")
    @POST("${LOGGLY_API_URL}inputs/{token}/tag/android")
    fun log(@Path("token") token: String, @Body message: RequestBody): Call<LogglyResponse>


}
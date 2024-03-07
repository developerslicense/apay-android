package kz.airbapay.apay_android.network.loggly

import java.io.Serializable

/**
 * The response from Loggly's REST endpoints, which is a
 * JSON object, containing only a "response" key and its
 * value (normally equal to "ok" for success).
 *
 * @author tony19@gmail.com
 */
internal class LogglyResponse(
    val response: String? = null
) : Serializable

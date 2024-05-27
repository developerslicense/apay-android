package kz.airbapay.apay_android.network.loggly

import android.os.Build
import com.google.gson.annotations.SerializedName
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.DateUtils
import java.util.*

internal data class ApiLogEntry(

    @SerializedName("url")
    val url: String?,

    @SerializedName("method")
    val method: String?,

    @SerializedName("response_code")
    var responseCode: String?,

    @SerializedName("body_params")
    var bodyParams: String?,

    @SerializedName("response")
    var response: String?,

    @SerializedName("messages") // Если message без "s" на конце, почему-то пустое
    var message: String?,

    @SerializedName("platform")
    val platform: String = "Android",

    @SerializedName("phone")
    val phone: String? = DataHolder.userPhone,

    @SerializedName("time")
    val timestamp: String = DateUtils.toRFC3339(Date()),

    @SerializedName("model")
    val model: String = "${Build.BRAND} ${Build.MODEL}",

    @SerializedName("os_version")
    val osVersion: String = Build.VERSION.RELEASE,

    @SerializedName("SDK_version")
    val sdkVersion: String = DataHolder.sdkVersion,

    @SerializedName("page")
    var page: String = LoggerHelper.getPageName(),

    @SerializedName("orderNumber")
    var orderNumber: String = DataHolder.orderNumber,

    @SerializedName("invoiceId")
    var invoiceId: String = DataHolder.invoiceId,

    @SerializedName("env")
    var env: String = if (DataHolder.isProd) "PROD" else "TEST"

)

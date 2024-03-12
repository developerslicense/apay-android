package kz.airbapay.apay_android.data.utils

import android.annotation.SuppressLint
import kz.airbapay.apay_android.data.constant.RFC3339UTC
import java.text.SimpleDateFormat
import java.util.Date

internal object DateUtils {
    @SuppressLint("SimpleDateFormat")
    fun toRFC3339(date: Date?) = SimpleDateFormat(RFC3339UTC)
        .format(date ?: "")
        .replace("(\\d\\d)(\\d\\d)$".toRegex(), "$1:$2")

}
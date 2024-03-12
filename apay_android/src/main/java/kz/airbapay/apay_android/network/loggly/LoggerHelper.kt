package kz.airbapay.apay_android.network.loggly

import java.lang.ref.WeakReference

internal object LoggerHelper {

    private val loggerObserver: MutableList<WeakReference<LoggerObserver>> = ArrayList()

    fun addObserver(observer: LoggerObserver) {
        removeObserver(observer)
        loggerObserver.add(WeakReference(observer))
    }

    fun removeObserver(observer: LoggerObserver) {

        with(loggerObserver.iterator()) {
            forEach {
                if (it.get() == observer) { remove() }
            }
        }
    }

    fun getPageName(): String {
        return loggerObserver.firstOrNull()?.get()?.getPageName() ?: ""
    }

}

interface LoggerObserver {
    fun getPageName(): String
}
package kz.airbapay.apay_android.network.base

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build

internal object InternetConnectionUtils {
    fun isOnline(context: Context): Boolean {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            val connectivityManager =
                context.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

            val capabilities =
                connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

            return when {
                capabilities == null -> {
                    println("Internet Capabilities null")
                    false
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> {
                    println("Internet NetworkCapabilities.TRANSPORT_CELLULAR")
                    true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> {
                    println("Internet NetworkCapabilities.TRANSPORT_WIFI")
                    true
                }

                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> {
                    println("Internet NetworkCapabilities.TRANSPORT_ETHERNET")
                    true
                }

                else -> false
            }
        } else
            return true
    }
}
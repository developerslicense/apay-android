package kz.airbapay.apay_android.ui.pages.videoplayer

import android.annotation.SuppressLint
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.loggly.Logger

internal class VideoPlayerClient(
    private val inProgress: MutableState<Boolean>,
) : WebViewClient() {

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        Logger.log(
            message = "onReceivedSslError error $error",
        )
        if (DataHolder.isProd) handler.cancel() else handler.proceed()
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Logger.log(
            message = "onPageStarted $url",
        )
        inProgress.value = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Logger.log(
            message = "onPageFinished $url",
        )
        inProgress.value = false
    }
}




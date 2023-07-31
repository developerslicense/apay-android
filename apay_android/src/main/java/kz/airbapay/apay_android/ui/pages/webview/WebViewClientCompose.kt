package kz.airbapay.apay_android.ui.pages.webview

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.ui.pages.error.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.errorLog
import kz.airbapay.apay_android.data.utils.messageLog

internal class WebViewClientCompose(
    private val inProgress: MutableState<Boolean>,
    private val isRetry: MutableState<Boolean>,
    private val context: Context
) : WebViewClient() {
    /* final Map<String, String?>? data = ModalRoute.of(context)?.settings.arguments as Map<String, String?>?
        _action = data?["action"]
        _isRetry = data?["is_retry"] == "true" ? true : false*/

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        messageLog("onReceivedSslError error $error")
        if (DataHolder.isProd) handler.cancel() else handler.proceed()
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        messageLog("onPageStarted $url")
        inProgress.value = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        messageLog("onPageFinished, $url")
        inProgress.value = false
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: ""
        messageLog("shouldOverrideUrlLoading $url")

        when {
            url.contains("status=auth")
                    || url.contains("status=success") -> {
                messageLog("Status success")
                /*Navigator.pushNamed(
                    context,
                    routesSuccess,
                )*/
            }
            url.contains("status=error") -> {
                messageLog("3D secure status error")
                try {
                    val splitted = url.split(Regex("&"))
                    val result = splitted.first { element -> element.contains("errorCode") }
                    val resultSplitted = result.split(Regex("="))

                    val code = resultSplitted[1]
                        .replace("errorMsg", "") //todo временный костыль удаления "errorMsg" на период, пока не будет исправлено на бэке
                        .toInt()

                    openErrorPageWithCondition(
                        code,
                        context,
                        "",
                        isRetry.value
                    )
                } catch (e: Exception) {
                    errorLog(e)
                    openErrorPageWithCondition(0, context, null, false)
                }
            }

            else -> view?.loadUrl(url)
        }

        return false
    }
}




package kz.airbapay.apay_android.ui.pages.googlepay

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.errorLog
import kz.airbapay.apay_android.data.utils.messageLog
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess

internal class GooglePayClient(
    private val redirectUrl: String?,
    private val activity: Activity,
    private val inProgress: MutableState<Boolean>
) : WebViewClient() {

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

        if (url?.contains("https://accounts.youtube.com/accounts/") == true) {
            openAcquiring(
                redirectUrl = redirectUrl,
                activity = activity
            )
        }

        inProgress.value = false

    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: ""
        messageLog("shouldOverrideUrlLoading $url")

        when {
            url.contains("acquiring-api/sdk/api/v1/payments/three-ds") -> {
                openAcquiring(
                    redirectUrl = url,
                    activity = activity
                )
            }
            url.contains("status=auth")
                    || url.contains("status=success") -> {
                messageLog("Status success")
                openSuccess(activity)
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
                        errorCode = code,
                        activity = activity
                    )

                } catch (e: Exception) {
                    errorLog(e)
                    openErrorPageWithCondition(
                            errorCode = 0,
                            activity = activity
                    )
                }
            }

            else -> view?.loadUrl(url)
        }

        return false
    }
}


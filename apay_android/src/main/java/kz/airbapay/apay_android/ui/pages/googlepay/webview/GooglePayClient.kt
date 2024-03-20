package kz.airbapay.apay_android.ui.pages.googlepay.webview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.CountDownTimer
import android.webkit.SslErrorHandler
import android.webkit.WebResourceRequest
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openGooglePay
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.loggly.Logger

internal class GooglePayClient(
    private val redirectUrl: String?,
    private val activity: Activity,
    private val inProgress: MutableState<Boolean>
) : WebViewClient() {

    private var timer: CountDownTimer? = null

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
            message = "onPageFinished, $url",
        )

        if (url?.contains("https://accounts.youtube.com/accounts/") == true) {
            openGooglePay(
                redirectUrl = redirectUrl,
                activity = activity
            )
        } else if (url?.contains("airbapay.kz/sdk/google-pay-button") == true) {
            timer?.cancel()
            timer = object : CountDownTimer(5000, 1000) {
                override fun onTick(millisUntilFinished: Long) {}

                override fun onFinish() {
                    try {
                        loadJs(view)

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }
            timer?.start()
        } else {
            inProgress.value = false
        }

    }

    private fun loadJs(webView: WebView?) {
        webView?.loadUrl(
            """javascript:(function f() {
                            var btns = document.getElementsByTagName('button');
                            for (var i = 0, n = btns.length; i < n; i++) {
                              if (btns[i].getAttribute('aria-label') === 'Google Pay') {
                                btns[i].click();  
                              }
                            }
                          })()"""
        )
    }

    override fun shouldOverrideUrlLoading(
        view: WebView?,
        request: WebResourceRequest?
    ): Boolean {
        val url = request?.url?.toString() ?: ""

        Logger.log(
            message = "shouldOverrideUrlLoading $url",
        )

        when {
            url.contains("acquiring-api/sdk/api/v1/payments/three-ds") -> {
                Logger.log(
                    message = "Redirect to 3DS",
                )
                openAcquiring(
                    redirectUrl = url,
                    activity = activity
                )
            }

            url.contains("status=auth")
                    || url.contains("status=success") -> {
                Logger.log(
                    message = "Status success",
                )
                openSuccess(activity)
            }

            url.contains("status=error") -> {
                Logger.log(
                    message = "3D secure status error",
                )

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
                    e.printStackTrace()
                    openErrorPageWithCondition(
                        errorCode = 1,
                        activity = activity
                    )
                }
            }

            else -> view?.loadUrl(url)
        }

        return false
    }
}


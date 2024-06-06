package kz.airbapay.apay_android.ui.pages.external_webview

import android.annotation.SuppressLint
import android.app.Activity
import android.graphics.Bitmap
import android.net.http.SslError
import android.os.Build
import android.os.Message
import android.webkit.ClientCertRequest
import android.webkit.HttpAuthHandler
import android.webkit.RenderProcessGoneDetail
import android.webkit.SafeBrowsingResponse
import android.webkit.SslErrorHandler
import android.webkit.WebResourceError
import android.webkit.WebResourceRequest
import android.webkit.WebResourceResponse
import android.webkit.WebView
import android.webkit.WebViewClient
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.loggly.Logger

internal class ExternalStandardFlowWebViewClient(
    private val activity: Activity,
    private val inProgress: MutableState<Boolean>,
) : WebViewClient() {

    @SuppressLint("WebViewClientOnReceivedSslError")
    override fun onReceivedSslError(view: WebView, handler: SslErrorHandler, error: SslError) {
        Logger.log(
            message = "onReceivedSslError error $error",
            url = view.url
        )

        if (DataHolder.isProd) {
            handler.cancel()
            openErrorPageWithCondition(
                errorCode = 1,
                activity = activity
            )

        } else handler.proceed()
    }

    override fun onPageStarted(view: WebView, url: String, favicon: Bitmap?) {
        super.onPageStarted(view, url, favicon)
        Logger.log(
            message = "onPageStarted",
            url = url
        )
        inProgress.value = true
    }

    override fun onPageFinished(view: WebView?, url: String?) {
        super.onPageFinished(view, url)
        Logger.log(
            message = "onPageFinished",
            url = url
        )
        inProgress.value = false

        when {
            url?.contains(DataHolder.successBackUrl) == true -> {
                Logger.log(
                    message = "Status success",
                    url = url
                )
                openSuccess(activity)
            }

            url?.contains(DataHolder.failureBackUrl) == true -> {
                onFailure(url)
            }
        }
    }

    override fun shouldOverrideUrlLoading(view: WebView?, request: WebResourceRequest?): Boolean {
        val url = request?.url?.toString() ?: ""
        Logger.log(
            message = "shouldOverrideUrlLoading",
            url = url
        )
        when {
            url.contains("status=auth")
                    || url.contains("success") -> {
                Logger.log(
                    message = "Status success",
                    url = url
                )
                openSuccess(activity)
                return true
            }
            url.contains(DataHolder.failureBackUrl) -> {
                DataHolder.actionOnCloseProcessing?.invoke(activity, false)
                return true
            }
            url.contains("status=error")
                    || url.contains("failure") -> {
                onFailure(url)
                return true
            }

            else -> view?.loadUrl(url)
        }

        return false
    }

    private fun onFailure(url: String) {
        Logger.log(
            message = "3D secure status error",
            url = url
        )
        try {
            val splitted = url.split(Regex("&"))
            val result = splitted.first { element -> element.contains("errorCode") }
            val resultSplitted = result.split(Regex("="))

            val code = resultSplitted[1]
                .replace(
                    "errorMsg",
                    ""
                ) //todo временный костыль удаления "errorMsg" на период, пока не будет исправлено на бэке
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


    override fun onSafeBrowsingHit(
        view: WebView?,
        request: WebResourceRequest?,
        threatType: Int,
        callback: SafeBrowsingResponse?
    ) {
        super.onSafeBrowsingHit(view, request, threatType, callback)
        Logger.log(
            message = "onSafeBrowsingHit",
            url = view?.url
        )
    }

    override fun onRenderProcessGone(view: WebView?, detail: RenderProcessGoneDetail?): Boolean {
        Logger.log(
            message = "onRenderProcessGone",
            url = view?.url
        )
        return super.onRenderProcessGone(view, detail)
    }

    override fun onReceivedLoginRequest(
        view: WebView?,
        realm: String?,
        account: String?,
        args: String?
    ) {
        super.onReceivedLoginRequest(view, realm, account, args)
        Logger.log(
            message = "onReceivedLoginRequest | $realm | $account | $args |",
            url = view?.url
        )
    }

    override fun onReceivedHttpAuthRequest(
        view: WebView?,
        handler: HttpAuthHandler?,
        host: String?,
        realm: String?
    ) {
        super.onReceivedHttpAuthRequest(view, handler, host, realm)
        Logger.log(
            message = "onReceivedHttpAuthRequest | $host | $realm |",
            url = view?.url
        )
    }

    override fun onReceivedClientCertRequest(view: WebView?, request: ClientCertRequest?) {
        super.onReceivedClientCertRequest(view, request)
        Logger.log(
            message = "onReceivedClientCertRequest",
            url = view?.url
        )
    }

    override fun onFormResubmission(view: WebView?, dontResend: Message?, resend: Message?) {
        super.onFormResubmission(view, dontResend, resend)
        Logger.log(
            message = "onFormResubmission | $dontResend | $resend |",
            url = view?.url
        )
    }

    override fun onReceivedHttpError(
        view: WebView?,
        request: WebResourceRequest?,
        errorResponse: WebResourceResponse?
    ) {
        super.onReceivedHttpError(view, request, errorResponse)
        Logger.log(
            message = "onReceivedHttpError  ${errorResponse?.statusCode}",
            url = view?.url
        )
    }

    override fun onReceivedError(
        view: WebView?,
        request: WebResourceRequest?,
        error: WebResourceError?
    ) {
        super.onReceivedError(view, request, error)

        val textError = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            "ErrorCode = ${error?.errorCode} | Description ${error?.description}"
        } else {
            ""
        }
        Logger.log(
            message = "onReceivedError $textError",
            url = view?.url
        )
    }

    override fun onReceivedError(
        view: WebView?,
        errorCode: Int,
        description: String?,
        failingUrl: String?
    ) {
        super.onReceivedError(view, errorCode, description, failingUrl)
        Logger.log(
            message = "onReceivedError deprecated $errorCode | $failingUrl",
            url = view?.url
        )
    }

    override fun onTooManyRedirects(view: WebView?, cancelMsg: Message?, continueMsg: Message?) {
        super.onTooManyRedirects(view, cancelMsg, continueMsg)
        val url = view?.url

        Logger.log(
            message = "onTooManyRedirects deprecated. CancelMessage $cancelMsg. ContinueMessage $continueMsg",
            url = url
        )
    }

    override fun shouldOverrideUrlLoading(view: WebView?, url: String?): Boolean {
        Logger.log(
            message = "shouldOverrideUrlLoading deprecated",
            url = url
        )
        return super.shouldOverrideUrlLoading(view, url)
    }
}




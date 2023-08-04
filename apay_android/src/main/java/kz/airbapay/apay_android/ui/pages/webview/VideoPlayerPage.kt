package kz.airbapay.apay_android.ui.pages.webview

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.viewinterop.AndroidView

@Composable
internal fun VideoPlayerPage(
    url: String?
) {
    val inProgress = remember { mutableStateOf(true) }

    AndroidView(
        factory = {
            WebView(it).apply {
                layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
                )

                clearCache(true)

                settings.javaScriptEnabled = true
                settings.domStorageEnabled = true
                settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.SINGLE_COLUMN
                settings.loadWithOverviewMode = true
                settings.useWideViewPort = true

                webChromeClient = WebChromeClient()
                webViewClient = WebViewClientCompose(
                    inProgress = inProgress,
                    isRetry = false
                )

                loadPage(url)
            }
        },
        update = {
            it.loadPage(url)
        }
    )
}

private fun WebView.loadPage(
    url: String?
) {
//   val url = "https://www.youtube.com/embed/XUTXU6fy94E" //для теста видео

    url?.let {
        val dataUrl = formatHtml(url)
        loadData(dataUrl, "text/html", "utf-8")
    }
//        ?: NavigationObserver.showSnackBar(getStr(LN_GLOBAL_ERROR_SOMETHING_WENT_WRONG))
}

private fun formatHtml(url: String?) = "<html>" +
        "<body>" +
        "<br>" +
        "<iframe src=\"" + url + "\" style=\"position:fixed; top:0; left:0; bottom:0; right:0; width:100%; height:100%; border:none; margin:0; padding:0; overflow:hidden; z-index:999999;\"/>" +
        "</body>" +
        "</html>"

package kz.airbapay.apay_android.ui.pages.webview

import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.viewinterop.AndroidView
import androidx.navigation.NavController
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.pages.dialog.InitDialogExit
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

@Composable
internal fun WebViewPage(
    url: String?,
    navController: NavController
) {
    val inProgress = remember { mutableStateOf(true) }

    val showDialogExit = remember {
        mutableStateOf(false)
    }

    BackHandler {
        showDialogExit.value = true
    }

    Column(
        modifier = Modifier.fillMaxSize()
    ) {

        ViewToolbar(
            title = "",
            backIcon = R.drawable.cancel,
            actionBack = {

            }
        )

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
                        navController = navController,
                        inProgress = inProgress,
                    )

                    loadPage(url)
                }
            },
            update = {
                it.loadPage(url)
            }
        )
    }

    if (showDialogExit.value) {
        InitDialogExit(
            onDismissRequest = {
                showDialogExit.value = false
            }
        )
    }
}

private fun WebView.loadPage(
    url: String?
) {

    url?.let {
        loadUrl(url)
    }
//        ?: showMessage(getStr(LN_GLOBAL_ERROR_SOMETHING_WENT_WRONG))
}



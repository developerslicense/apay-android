package kz.airbapay.apay_android.ui.pages.acquiring

import android.annotation.SuppressLint
import android.app.Activity
import android.os.Bundle
import android.view.ViewGroup
import android.webkit.WebChromeClient
import android.webkit.WebSettings
import android.webkit.WebView
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.viewinterop.AndroidView
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.loggly.Logger
import kz.airbapay.apay_android.ui.pages.BaseActivity
import kz.airbapay.apay_android.ui.pages.dialogs.InitDialogExit
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

internal class AcquiringActivity: BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val url = intent.getStringExtra(ARG_ACTION)

        Logger.log(
            message = "onCreate AcquiringActivity. url = $url",
        )

        setContent {
            AcquiringPage(url = url)
        }
    }

    override fun getPageName() = this.localClassName
}

@SuppressLint("SetJavaScriptEnabled")
@Composable
internal fun AcquiringPage(
    url: String?
) {
    val activity = LocalContext.current as Activity
    val inProgress = remember { mutableStateOf(true) }

    val showDialogExit = remember {
        mutableStateOf(false)
    }

    BackHandler {
        showDialogExit.value = true
    }

    ConstraintLayout {

        Column(
            modifier = Modifier.fillMaxSize()
        ) {

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
                        settings.layoutAlgorithm = WebSettings.LayoutAlgorithm.NORMAL
                        settings.loadWithOverviewMode = true
                        settings.useWideViewPort = true

                        webChromeClient = WebChromeClient()
                        webViewClient = AcquiringClient(
                            activity = activity,
                            inProgress = inProgress
                        )

                        loadPage(url)
                    }
                },
                update = {
//                    it.loadPage(url)
                }
            )
        }

        ViewToolbar(
            title = "",
            backIcon = R.drawable.cancel,
            actionBack = {
                showDialogExit.value = true
            }
        )

        if (inProgress.value && !DataHolder.enabledLogsForProd) {
            ProgressBarView()
        }
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



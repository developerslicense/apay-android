package kz.airbapay.apay_android.ui.pages.googlepay.webview

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc
import kz.airbapay.apay_android.ui.ui_components.initAuth

@Composable
internal fun GPayWebView(
    activity: Activity,
    coroutineScope: CoroutineScope,
    openGooglePayForWebFlow: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(48.dp)
            .background(
                color = ColorsSdk.bgGPAY,
                shape = RoundedCornerShape(
                    topStart = 8.dp,
                    topEnd = 8.dp,
                    bottomEnd = 8.dp,
                    bottomStart = 8.dp
                )
            )
            .clickable {
                initAuth(
                    activity = activity,
                    coroutineScope = coroutineScope,
                    onSuccess = {
                        openGooglePayForWebFlow()
                    },
                    onFailed = {},
                    onNotSecurity = {}
                )

            }
    ) {
        LoadImageSrc(imageSrc = R.drawable.g_pay)
    }
}
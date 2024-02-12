package kz.airbapay.apay_android.ui.ui_components

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.utils.checkIsDeviceSecure
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun GPayView(
    openGooglePay: () -> Unit
) {
    val activity = LocalContext.current as Activity
    val coroutineScope = rememberCoroutineScope()

    val isDeviceSecure = checkIsDeviceSecure(activity)

    if (isDeviceSecure) {
        Spacer(modifier = Modifier.height(16.dp))

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
                            openGooglePay()
                        },
                        onFailed = {},
                        onNotSecurity = {}
                    )

                }
        ) {
            LoadImageSrc(imageSrc = R.drawable.g_pay)
        }
    }
}
package kz.airbapay.apay_android.ui.pages.dialog

import android.app.Activity
import android.view.Gravity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import androidx.compose.ui.window.DialogWindowProvider
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.dropPayment
import kz.airbapay.apay_android.data.constant.dropPaymentDescription
import kz.airbapay.apay_android.data.constant.no
import kz.airbapay.apay_android.data.constant.yes
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun InitDialogExit(
    onDismissRequest: () -> Unit
) {
    val context = LocalContext.current

    Dialog(
        onDismissRequest = onDismissRequest,
        properties = DialogProperties(
            dismissOnBackPress = true,
            dismissOnClickOutside = true
        )
    ) {
        val dialogWindowProvider = LocalView.current.parent as DialogWindowProvider
        dialogWindowProvider.window.setGravity(Gravity.BOTTOM)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .background(
                    color = ColorsSdk.bgBlock,
                    shape = RoundedCornerShape(
                        topStart = 8.dp,
                        topEnd = 8.dp,
                        bottomEnd = 8.dp,
                        bottomStart = 8.dp
                    )
                )
                .padding(
                    horizontal = 16.dp,
                    vertical = 24.dp
                )
        ) {
            Image(
                painter = painterResource(R.drawable.warning_red_oval),
                contentDescription = "pay_failed",
                modifier = Modifier
                    .fillMaxWidth(0.5f)
            )

            Text(
                text = dropPayment(),
                style = LocalFonts.current.subtitleBold,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(top = 16.dp)
            )

            Text(
                text = dropPaymentDescription(),
                style = LocalFonts.current.bodyRegular,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 8.dp)
            )

            ViewButton(
                title = no(),
                actionClick = {
                    onDismissRequest()
                },
                modifierRoot = Modifier.padding(top = 24.dp)
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViewButton(
                title = yes(),
                textColor = ColorsSdk.colorBrandMainMS.value,
                backgroundColor = ColorsSdk.colorBrandInversionMS.value,
                actionClick = {
                    onDismissRequest()
                    (context as Activity).finish()
                }
            )
        }
    }
}
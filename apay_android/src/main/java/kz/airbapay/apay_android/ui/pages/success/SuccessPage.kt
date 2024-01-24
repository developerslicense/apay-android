package kz.airbapay.apay_android.ui.pages.success

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.goToMarker
import kz.airbapay.apay_android.data.constant.paySuccess
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewButton

internal class SuccessActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SuccessPage()
        }
    }
}

@Composable
internal fun SuccessPage() {
    val activity = LocalContext.current as Activity

    BackHandler {
        activity.setResult(Activity.RESULT_CANCELED)
        activity.finish()
    }

    ConstraintLayout(
        modifier = Modifier
            .background(ColorsSdk.bgMain)
            .clipToBounds()
            .fillMaxSize()
    ) {

        val (spaceRef, iconRef, textRef, buttonRef) = createRefs()

        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .constrainAs(spaceRef) {
                    top.linkTo(parent.top)
                }
        )
        Image(
            painter = painterResource(R.drawable.pay_success),
            contentDescription = "pay_success",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .constrainAs(iconRef) {
                    top.linkTo(spaceRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = paySuccess(),
            style = LocalFonts.current.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(iconRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        ViewButton(
            title = goToMarker(),
            actionClick = {
                DataHolder.frontendCallback?.invoke(activity, true)
                DataHolder.frontendCallback = null
            },
            modifierRoot = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 20.dp)
                .constrainAs(buttonRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}
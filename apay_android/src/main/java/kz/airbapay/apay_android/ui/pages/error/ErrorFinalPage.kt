package kz.airbapay.apay_android.ui.pages.error

import android.app.Activity
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
import kz.airbapay.apay_android.data.constant.timeForPayExpired
import kz.airbapay.apay_android.data.constant.tryFormedNewCart
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun ErrorFinalPage() {
    val context = LocalContext.current

    BackHandler {
        (context as Activity).finish()
    }

    ConstraintLayout(
        modifier = Modifier
            .background(ColorsSdk.bgMain)
            .clipToBounds()
            .fillMaxSize()
    ) {

        val (spaceRef, iconRef, textRef, text2Ref, buttonRef) = createRefs()
        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.25f)
                .constrainAs(spaceRef) {
                    top.linkTo(parent.top)
                }
        )

        Image(
            painter = painterResource(R.drawable.pay_failed),
            contentDescription = "pay_failed",
            modifier = Modifier
                .fillMaxWidth(0.5f)
                .constrainAs(iconRef) {
                    top.linkTo(spaceRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = timeForPayExpired(),
            style = LocalFonts.current.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(iconRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = tryFormedNewCart(),
            style = LocalFonts.current.bodyRegular,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(text2Ref) {
                    top.linkTo(textRef.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        ViewButton(
            title = goToMarker(),
            actionClick = {
                (context as Activity).finish()
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
package kz.airbapay.apay_android.ui.pages.error

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.buttonBottom
import kz.airbapay.apay_android.data.constant.buttonTop
import kz.airbapay.apay_android.data.constant.clickOnBottom
import kz.airbapay.apay_android.data.constant.clickOnTop
import kz.airbapay.apay_android.data.constant.description
import kz.airbapay.apay_android.data.constant.message
import kz.airbapay.apay_android.ui.pages.dialog.InitDialogExit
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun ErrorPage(
    errorCode: ErrorsCode,
    navController: NavController
) {

    val context = LocalContext.current
    val showDialogExit = remember {
        mutableStateOf(false)
    }

    BackHandler {
        showDialogExit.value = true
    }

    ConstraintLayout(
        modifier = Modifier
            .background(ColorsSdk.bgBlock)
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
            text = errorCode.message(),
            style = LocalFonts.current.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(iconRef.bottom, margin = 24.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        if (errorCode.description().isNotBlank())
            Text(
                text = errorCode.description(),
                style = LocalFonts.current.bodyRegular,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .constrainAs(text2Ref) {
                        top.linkTo(textRef.bottom, margin = 8.dp)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

        Column(
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .padding(vertical = 20.dp)
                .constrainAs(buttonRef) {
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        ) {

            ViewButton(
                title = errorCode.buttonTop(),
                actionClick = {
                    errorCode.clickOnTop(
                        navController = navController,
                        finish = { (context as Activity).finish() }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViewButton(
                title = errorCode.buttonBottom(),
                isMainBrand = false,
                actionClick = {
                    errorCode.clickOnBottom(
                        navController = navController,
                        finish = { (context as Activity).finish() }
                    )
                }
            )
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
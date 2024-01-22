package kz.airbapay.apay_android.ui.pages.error

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
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
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.BanksName
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.buttonBottom
import kz.airbapay.apay_android.data.constant.buttonTop
import kz.airbapay.apay_android.data.constant.clickOnBottom
import kz.airbapay.apay_android.data.constant.clickOnTop
import kz.airbapay.apay_android.data.constant.description
import kz.airbapay.apay_android.data.constant.forChangeLimitInHomebank
import kz.airbapay.apay_android.data.constant.forChangeLimitInKaspi
import kz.airbapay.apay_android.data.constant.message
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.ui.pages.dialogs.InitDialogExit
import kz.airbapay.apay_android.ui.pages.videoplayer.VideoPlayerPage
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun ErrorWithInstructionPage(
    errorCode: ErrorsCode
) {
    val faqUrl = when (DataHolder.bankCode) {
        BanksName.kaspibank.name -> {
            if (DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang)
                "https://static-data.object.pscloud.io/pay-manuals/Kaspi_kaz.mp4"
            else "https://static-data.object.pscloud.io/pay-manuals/Kaspi_rus.mp4"
        }
        else -> {
            if(DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang)
                "https://static-data.object.pscloud.io/pay-manuals/Halyk_kaz.mp4"
            else "https://static-data.object.pscloud.io/pay-manuals/Halyk_rus.mp4"
        }
    }

    val activity = LocalContext.current as Activity

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

        val (iconRef, textRef, text2Ref, text3Ref, playerRef, buttonRef) = createRefs()

        Image(
            painter = painterResource(R.drawable.pay_failed),
            contentDescription = "pay_failed",
            modifier = Modifier
                .padding(top = 20.dp)
                .fillMaxWidth(0.3f)
                .constrainAs(iconRef) {
                    top.linkTo(parent.top)
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

        Text(
            text = errorCode.description(),
            style = LocalFonts.current.regular,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(horizontal = 16.dp)
                .constrainAs(text2Ref) {
                    top.linkTo(textRef.bottom, margin = 8.dp)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = if (DataHolder.bankCode == BanksName.kaspibank.name) forChangeLimitInKaspi() else forChangeLimitInHomebank(),
            style = LocalFonts.current.semiBold,
            textAlign = TextAlign.Start,
            modifier = Modifier
                .constrainAs(text3Ref) {
                    top.linkTo(text2Ref.bottom, margin = 32.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                }
        )

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(162.dp)
                .padding(horizontal = 16.dp)
                .constrainAs(playerRef) {
                    top.linkTo(text3Ref.bottom, margin = 16.dp)
                    start.linkTo(parent.start, margin = 16.dp)
                    end.linkTo(parent.end, margin = 16.dp)
                }
        ){
            VideoPlayerPage(faqUrl)
        }

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
                        activity = activity,
                        finish = { activity.finish() }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            ViewButton(
                title = errorCode.buttonBottom(),
                isMainBrand = false,
                actionClick = {
                    errorCode.clickOnBottom(
                        activity = activity,
                        finish = {
                            DataHolder.frontendCallback?.invoke(false)
                        }
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
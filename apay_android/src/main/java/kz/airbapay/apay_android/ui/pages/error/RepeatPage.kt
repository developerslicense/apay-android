package kz.airbapay.apay_android.ui.pages.error

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.thisNeedSomeTime
import kz.airbapay.apay_android.data.constant.weRepeatYourPayment
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler

internal class RepeatActivity: ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            RepeatPage(paymentsRepository = Repository.paymentsRepository)
        }
    }
}

@Composable
internal fun RepeatPage(
    paymentsRepository: PaymentsRepository?
) {
    val activity = LocalContext.current as Activity
    BackHandler {}

    ConstraintLayout(
        modifier = Modifier
            .background(ColorsSdk.bgMain)
            .clipToBounds()
            .fillMaxSize()
    ) {

        val (spaceRef, progressRef, textRef, text2Ref) = createRefs()

        Spacer(
            modifier = Modifier
                .fillMaxHeight(0.35f)
                .constrainAs(spaceRef) {
                    top.linkTo(parent.top)
                }
        )

        Text(
            text = weRepeatYourPayment(),
            style = LocalFonts.current.h3,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .constrainAs(textRef) {
                    top.linkTo(spaceRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        Text(
            text = thisNeedSomeTime(),
            style = LocalFonts.current.bodyRegular,
            textAlign = TextAlign.Center,
            modifier = Modifier
                .padding(top = 16.dp)
                .constrainAs(text2Ref) {
                    top.linkTo(textRef.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )

        CircularProgressIndicator(
            color = ColorsSdk.colorBrand,
            modifier = Modifier
                .padding(top = 24.dp)
                .fillMaxWidth(0.3f)
                .constrainAs(progressRef) {
                    top.linkTo(text2Ref.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }

    LaunchedEffect("onStart") {
        launch {
            onStart(
                activity = activity,
                paymentsRepository = paymentsRepository
            )
        }
    }
}

private fun onStart(
    activity: Activity,
    paymentsRepository: PaymentsRepository?
) {

    paymentsRepository?.paymentAccountEntryRetry(
        result = { response ->
            if (response.isSecure3D == true) {
                openAcquiring(
                    redirectUrl = response.secure3D?.action,
                    activity = activity
                )

            } else if (response.errorCode != "0") {
                openErrorPageWithCondition(
                    errorCode = response.errorCode?.toInt() ?: 1,
                    activity = activity
                )

            } else {
                openSuccess(activity)
            }
        },
        error = {
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        }
    )

}
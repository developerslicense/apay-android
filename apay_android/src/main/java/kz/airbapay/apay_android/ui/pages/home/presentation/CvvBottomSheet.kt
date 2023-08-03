package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.cvvInfo
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.InitActionIcon
import kz.airbapay.apay_android.ui.ui_components.LoadImageSrc

@Composable
internal fun CvvBottomSheet(
    actionClose: () -> Unit
) {

    Card(
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,

            ) {

            InitHeader(
                title = "CVV",
                actionClose = actionClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorsSdk.gray0)
            )
        }
    }
}


@Composable
internal fun InitHeader(
    title: String,
    actionClose: (() -> Unit),
    modifier: Modifier
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(ColorsSdk.gray0),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        LoadImageSrc(
            imageSrc = R.drawable.line_gray,
            modifier = Modifier
                .padding(top = 6.dp)
                .align(alignment = Alignment.CenterHorizontally)
        )
    }

    ConstraintLayout(
        modifier = modifier
    ) {
        val (titleRef, closeRef) = createRefs()

        Text(
            text = title,
            style = LocalFonts.current.subtitleBold,
            modifier = Modifier
                .constrainAs(titleRef) {
                    top.linkTo(parent.top)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)

                }
                .padding(top = 16.dp)
        )

        InitActionIcon(
            action = actionClose,
            iconSrc = R.drawable.cancel,
            modifier = Modifier
                .constrainAs(closeRef) {
                    end.linkTo(parent.end)
                    top.linkTo(parent.top)
                }
                .padding(
                    end = 12.dp,
                    top = 16.dp
                )
        )
    }

    Text(
        text = cvvInfo(),
        style = LocalFonts.current.regular,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .padding(bottom = 32.dp)
            .padding(top = 22.dp)
    )
}

package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun InitHeader(
    title: String,
    modifier: Modifier,
    actionClose: (() -> Unit)?
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

            if (actionClose != null) {
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
        }

        Spacer(modifier = Modifier.height(6.dp))

        Spacer(
            modifier = Modifier
                .fillMaxWidth()
                .height(1.dp)
                .background(ColorsSdk.gray5)
        )
    }
}

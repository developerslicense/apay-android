package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun ProgressBarView() {

    BackHandler {}

    ConstraintLayout(
        modifier = Modifier
            .background(ColorsSdk.bgMain)
            .clipToBounds()
            .fillMaxSize()
    ) {

        val (progressRef) = createRefs()

        CircularProgressIndicator(
            color = ColorsSdk.colorBrandMainMS.value,
            modifier = Modifier
                .fillMaxWidth(0.3f)
                .constrainAs(progressRef) {
                    top.linkTo(parent.top)
                    bottom.linkTo(parent.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
        )
    }
}
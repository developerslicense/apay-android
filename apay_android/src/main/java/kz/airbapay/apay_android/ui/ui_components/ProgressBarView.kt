package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.IntSize
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.ui.resources.ColorsSdk

@Composable
internal fun ProgressBarView(
    modifier: Modifier = Modifier.fillMaxSize(),
    size: MutableState<IntSize> = remember {
        mutableStateOf(IntSize.Zero)
    }
) {

    BackHandler {}

    ConstraintLayout(
        modifier = modifier
            .then(
                with(LocalDensity.current) {
                    Modifier.size(
                        width = size.value.width.toDp(),
                        height = size.value.height.toDp(),
                    )
                }
            )
            .background(ColorsSdk.bgMain)
            .clipToBounds()
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
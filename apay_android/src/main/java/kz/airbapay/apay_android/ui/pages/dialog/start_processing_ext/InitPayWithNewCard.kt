package kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.payAnotherCard
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun InitViewStartProcessingPayWithNewCard(
    actionClick: (() -> Unit)
) {

    Card(
        shape = RoundedCornerShape(8.dp),
        elevation = 0.dp,
        border = BorderStroke(
            width = 0.1.dp,
            color = ColorsSdk.gray10
        ),
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxWidth()
            .height(32.dp)
            .clickable(
                role = Role.Button,
                enabled = true,
                onClick = { actionClick() }
            )

    ) {
        ConstraintLayout(
            modifier = Modifier.fillMaxWidth(),
        ) {
            val (plusRef, titleRef) = createRefs()

            Image(
                painter = painterResource(R.drawable.ic_add),
                contentDescription = "ic_add",
                modifier = Modifier
                    .size(16.dp)
                    .constrainAs(plusRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        end.linkTo(titleRef.start, margin = 12.dp)
                    },
                colorFilter = ColorFilter.tint(color = ColorsSdk.colorBrandMainMS.value)
            )

            Text(
                text = payAnotherCard(),
                color = ColorsSdk.colorBrandMainMS.value,
                style = LocalFonts.current.semiBold,
                modifier = Modifier
                    .constrainAs(titleRef) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

        }
    }

    Spacer(modifier = Modifier.height(16.dp))
}

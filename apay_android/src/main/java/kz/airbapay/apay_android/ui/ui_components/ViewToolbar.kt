package kz.airbapay.apay_android.ui.ui_components

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun ViewToolbar(
    title: String? = null,
    actionBack: (() -> Unit)? = null,
    backIcon: Int,
    modifier: Modifier = Modifier
) {

    actionBack?.let {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = modifier
                .fillMaxWidth()
                .defaultMinSize(minHeight = 56.dp)
                .padding(
                    top = 8.dp,
                    end = 12.dp,
                    start = 12.dp
                )

        ) {

            InitActionIcon(
                action = actionBack,
                _outlinedButtonColor = ColorsSdk.bgBlock,
                iconSrc = backIcon,
                modifier = modifier
            )

            Text(
                text = title.orEmpty(),
                style = LocalFonts.current.h0,
                overflow = TextOverflow.Ellipsis,
                maxLines = 1,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp)
            )
        }
    }
}

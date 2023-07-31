package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.Switch
import androidx.compose.material.SwitchDefaults
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.ui.resources.LocalFonts

@Composable
internal fun SwitchedView(
    text: String,
    switchCheckedState: MutableState<Boolean>,
    actionOnTrue: () -> Unit,
    actionOnFalse: () -> Unit
) {
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier
            .padding(horizontal = 16.dp)
            .fillMaxSize()
            .height(48.dp)
    ) {
        Text(
            text = text,
            style = LocalFonts.current.regular
        )
        Image(
            painter = painterResource(if (switchCheckedState.value) R.drawable.switch_on else R.drawable.switch_off),
            contentDescription = "switcher",
            modifier = Modifier
                .height(24.dp)
                .width(40.dp)
                .clickable {
                    switchCheckedState.value = !switchCheckedState.value

                    if (switchCheckedState.value) {
                        actionOnTrue()
                    } else {
                        actionOnFalse()
                    }
                }
        )
    }
}
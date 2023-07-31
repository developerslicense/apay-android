package kz.airbapay.apay_android.ui.pages.home

import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.paymentOfPurchase
import kz.airbapay.apay_android.ui.pages.dialog.InitDialogExit
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

@Composable
internal fun HomePage(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scrollState: ScrollState = rememberScrollState()
) {
    val context = LocalContext.current
    val showDialogExit = remember {
        mutableStateOf(false)
    }

    BackHandler {
        showDialogExit.value = true
    }

    Column(
        modifier = Modifier
            .verticalScroll(scrollState)
    ) {
        ViewToolbar(
            title = paymentOfPurchase(),
            backIcon = R.drawable.ic_arrow_back,
            actionBack = {
                showDialogExit.value = true
            }
        )


    }

    if (showDialogExit.value) {
        InitDialogExit(
            onDismissRequest = {
                showDialogExit.value = false
            }
        )
    }
}




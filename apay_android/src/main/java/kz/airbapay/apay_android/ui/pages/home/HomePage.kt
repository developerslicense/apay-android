package kz.airbapay.apay_android.ui.pages.home

import androidx.compose.foundation.Image
import androidx.compose.foundation.ScrollState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.paymentOfPurchase
import kz.airbapay.apay_android.data.constant.saveCardData
import kz.airbapay.apay_android.data.constant.sendCheckToEmail
import kz.airbapay.apay_android.ui.pages.dialog.InitDialogExit
import kz.airbapay.apay_android.ui.pages.home.presentation.CardNumberView
import kz.airbapay.apay_android.ui.pages.home.presentation.CvvView
import kz.airbapay.apay_android.ui.pages.home.presentation.DateExpiredView
import kz.airbapay.apay_android.ui.pages.home.presentation.EmailView
import kz.airbapay.apay_android.ui.pages.home.presentation.NameHolderView
import kz.airbapay.apay_android.ui.pages.home.presentation.SwitchedView
import kz.airbapay.apay_android.ui.pages.home.presentation.TopInfoView
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

@Composable
internal fun HomePage(
    coroutineScope: CoroutineScope = rememberCoroutineScope(),
    scrollState: ScrollState = rememberScrollState()
) {
    val context = LocalContext.current
    val showDialogExit = remember { mutableStateOf(false) }
    val switchSaveCard = remember { mutableStateOf(false) }
    val switchSendToEmail = remember { mutableStateOf(false) }

    BackHandler {
        showDialogExit.value = true
    }

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = Modifier
            .fillMaxSize()
            .verticalScroll(scrollState)
            .background(ColorsSdk.bgBlock)
    ) {
        ViewToolbar(
            title = paymentOfPurchase(),
            backIcon = R.drawable.ic_arrow_back,
            actionBack = {
                showDialogExit.value = true
            }
        )

        Spacer(modifier = Modifier.height(24.dp))
        Image(
            painter = painterResource(R.drawable.top_info),
            contentDescription = "top_info",
            modifier = Modifier
                .padding(horizontal = 46.dp)
                .height(32.dp)
        )

        TopInfoView()

        Spacer(modifier = Modifier.height(16.dp))
        CardNumberView()

        Spacer(modifier = Modifier.height(16.dp))
        NameHolderView()

        Spacer(modifier = Modifier.height(16.dp))
        Row(
          modifier = Modifier
              .padding(start = 16.dp, end = 16.dp)
              .fillMaxWidth()
        ) {
            DateExpiredView(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(end = 6.dp)
            )
            CvvView(
                modifier = Modifier
                    .weight(0.5f)
                    .padding(start = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
        SwitchedView(
            text = saveCardData(),
            switchCheckedState = switchSaveCard,
            actionOnTrue = { },
            actionOnFalse = { }
        )

        SwitchedView(
            text = sendCheckToEmail(),
            switchCheckedState = switchSendToEmail,
            actionOnTrue = { },
            actionOnFalse = { }
        )

        if (switchSendToEmail.value) {
            EmailView()
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



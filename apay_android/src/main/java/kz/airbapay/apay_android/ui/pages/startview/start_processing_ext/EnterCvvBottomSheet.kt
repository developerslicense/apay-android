package kz.airbapay.apay_android.ui.pages.startview.start_processing_ext

import android.app.Activity
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import kz.airbapay.apay_android.data.constant.cardNumber
import kz.airbapay.apay_android.data.constant.cvvEnter
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.constant.wrongCvv
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.getMoneyFormatted
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.ui.pages.home.presentation.CvvView
import kz.airbapay.apay_android.ui.pages.startview.bl.startSavedCard
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.ViewButton

@Composable
internal fun EnterCvvBottomSheet(
    actionClose: () -> Unit,
    showCvv: () -> Unit,
    cardMasked: String?,
    cardId: String?,
    isLoading: MutableState<Boolean>,
    cvvError: MutableState<String?>
) {

    val activity = LocalContext.current as Activity
    val cvvText = remember { mutableStateOf(TextFieldValue("")) }
    val cvvFocusRequester = FocusRequester()

    Card(
        shape = RoundedCornerShape(
            topStart = 12.dp,
            topEnd = 12.dp
        ),
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            InitHeader(
                title = cvvEnter(),
                actionClose = actionClose,
                modifier = Modifier
                    .fillMaxWidth()
                    .background(ColorsSdk.gray0)
            )

            Row(
                horizontalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .recomposeHighlighter()
                    .padding(horizontal = 16.dp)
                    .padding(top = 16.dp)
                    .fillMaxWidth()
                    .background(
                        color = ColorsSdk.bgMain,
                        shape = RoundedCornerShape(
                            topStart = 8.dp,
                            topEnd = 8.dp,
                            bottomEnd = 8.dp,
                            bottomStart = 8.dp
                        )
                    )
                    .padding(16.dp)
            ) {
                Text(
                    text = cardNumber(),
                    textAlign = TextAlign.Start,
                    style = LocalFonts.current.regular,
                    modifier = Modifier.recomposeHighlighter()
                )
                cardMasked?.let {
                    Text(
                        text = cardMasked,
                        textAlign = TextAlign.End,
                        style = LocalFonts.current.semiBold,
                        modifier = Modifier.recomposeHighlighter()
                    )
                }
            }

            CvvView(
                cvvError = cvvError,
                cvvFocusRequester = cvvFocusRequester,
                cvvText = cvvText,
                actionClickInfo = { //todo
                    /*coroutineScope.launch {
                        sheetState.show()
                    }*/
                },
                modifier = Modifier
                    .padding(top = 24.dp)
                    .padding(horizontal = 16.dp)
            )


            ViewButton(
                title = "${payAmount()} ${getMoneyFormatted(DataHolder.purchaseAmount)}",
                actionClick = {
                    if (cvvText.value.text.length == 3) {
                        cvvError.value = null
                        actionClose()
                        isLoading.value = true
                        startSavedCard(
                            activity = activity,
                            cardId = cardId ?: "",
                            cvv = cvvText.value.text,
                            showCvv = showCvv,
                            isError = cvvError,
                            isLoading = isLoading
                        )
                    } else {
                        cvvError.value = wrongCvv()
                    }
                },
                modifierRoot = Modifier
                    .recomposeHighlighter()
                    .padding(horizontal = 16.dp)
                    .padding(top = 24.dp)
                    .padding(bottom = 32.dp)
            )
            /* Text(
                text = cvvInfo(),
                style = LocalFonts.current.regular,
                modifier = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 32.dp)
                    .padding(top = 22.dp)
            )*/
        }
    }
}


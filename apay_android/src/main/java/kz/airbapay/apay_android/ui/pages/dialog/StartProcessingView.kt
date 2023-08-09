package kz.airbapay.apay_android.ui.pages.dialog

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.constant.somethingWentWrong
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.ClientConnector
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.CardRepository
import kz.airbapay.apay_android.network.repository.startAuth
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitDialogStartProcessingAmount
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitDialogStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitDialogStartProcessingCards
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitDialogStartProcessingGPay
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.resources.LocalFonts
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

@Composable
internal fun StartProcessingView(
    needShowProgressBar: Boolean = true,
    actionClose: () -> Unit,
    actionOnLoadingCompleted: () -> Unit = {},
    isBottomSheetType: Boolean = true,
    backgroundColor: Color = ColorsSdk.bgBlock
) {
    val context = LocalContext.current
    val clientConnector = ClientConnector(context)
    val api = clientConnector.retrofit.create(Api::class.java)
    val cardRepository = CardRepository(api)
    val authRepository = AuthRepository(api)

    if (isBottomSheetType) {
        BackHandler {
            actionClose()
        }
    }

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()

    val isError = remember { mutableStateOf(false) }
    val size = remember { mutableStateOf(IntSize.Zero) }
    val showProgressBar = remember { mutableStateOf(true) }
    val selectedCard = rememberSaveable { mutableStateOf<BankCard?>(null) }

    val savedCards = remember {
        mutableStateOf<List<BankCard>>(emptyList())
    }

    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(
            topStart = if (isBottomSheetType) 12.dp else 0.dp,
            topEnd = if (isBottomSheetType) 12.dp else 0.dp
        ),
        modifier = Modifier
            .fillMaxWidth()
            .onSizeChanged {
                size.value = it
            },
        elevation = if(isBottomSheetType) 5.dp else 0.dp
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            if (isBottomSheetType) {
                InitHeader(
                    title = paymentByCard(),
                    actionClose = actionClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorsSdk.gray0)
                )
            }

            if (isError.value) {
                InitErrorState()

            } else {
                InitDialogStartProcessingAmount(purchaseAmount.value)
                InitDialogStartProcessingGPay()

                if (savedCards.value.isNotEmpty()) {
                    InitDialogStartProcessingCards(
                        savedCards = savedCards.value,
                        selectedCard
                    )
                }

                InitDialogStartProcessingButtonNext(
                    savedCards = savedCards.value,
                    actionClose = actionClose,
                    purchaseAmount = purchaseAmount.value
                )
            }
        }

        if (showProgressBar.value
            && needShowProgressBar
        ) {
            ProgressBarView(
                size = size,
                modifier = Modifier.wrapContentHeight()
            )
        }
    }

    LaunchedEffect("CardRepository") {
        launch {
            isError.value = false

            startAuth(
                authRepository = authRepository,
                onError = {
                    isError.value = true
                    showProgressBar.value = false
                    actionOnLoadingCompleted()
                },
                onResult = {
                    cardRepository.getCards(
                        phone = DataHolder.userPhone,
                        error = {
                            showProgressBar.value = false
                            actionOnLoadingCompleted()
                        },
                        result = {
                            showProgressBar.value = false
                            savedCards.value = it
                            actionOnLoadingCompleted()
                        }
                    )
                }
            )
        }
    }
}

@Composable
private fun InitErrorState() {
    Image(
        painter = painterResource(R.drawable.something_wrong),
        contentDescription = "something_wrong",
        modifier = Modifier.fillMaxWidth(0.8f)
    )

    Text(
        text = somethingWentWrong(),
        style = LocalFonts.current.h3,
        textAlign = TextAlign.Center
    )
    Spacer(modifier = Modifier.height(20.dp))
}

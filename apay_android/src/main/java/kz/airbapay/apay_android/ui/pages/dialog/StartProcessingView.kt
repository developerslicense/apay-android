package kz.airbapay.apay_android.ui.pages.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.AirbaPayActivity
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.ClientConnector
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.CardRepository
import kz.airbapay.apay_android.network.repository.startAuth
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitErrorState
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingAmount
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingCards
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingGPay
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

@Composable
internal fun StartProcessingView(
    needShowProgressBar: Boolean = true,
    actionClose: () -> Unit,
    actionOnLoadingCompleted: () -> Unit = {},
    isBottomSheetType: Boolean = true,
    backgroundColor: Color = ColorsSdk.bgBlock,
    isAuthenticated: MutableState<Boolean>,
    customSuccessPage: @Composable (() -> Unit)? = null,
    sheetStateVisible: Boolean
) {
    val context = LocalContext.current
    val clientConnector = ClientConnector(context)
    val api = clientConnector.retrofit.create(Api::class.java)
    val cardRepository = CardRepository(api)
    val authRepository = AuthRepository(api)

    if (isBottomSheetType && sheetStateVisible) {
        BackHandler {
            actionClose()
        }
    }

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()

    val isError = remember { mutableStateOf(false) }
    val size = remember { mutableStateOf(IntSize.Zero) }
    val isLoading = remember { mutableStateOf(true) }
    val selectedCard = remember { mutableStateOf<BankCard?>(null) }

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
        elevation = if (isBottomSheetType) 5.dp else 0.dp
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
                InitViewStartProcessingAmount(purchaseAmount.value)
                InitViewStartProcessingGPay(
                    openGooglePay = {
                        actionClose()
                        AirbaPayActivity.init(
                            context = context,
                            customSuccessPage = customSuccessPage,
                            isGooglePay = true
                        )
                    }
                )

                if (savedCards.value.isNotEmpty()
                    && isAuthenticated.value
                ) {
                    InitViewStartProcessingCards(
                        savedCards = savedCards.value,
                        selectedCard = selectedCard,
                        actionClose = actionClose,
                        customSuccessPage = customSuccessPage
                    )
                }

                InitViewStartProcessingButtonNext(
                    savedCards = savedCards.value,
                    actionClose = actionClose,
                    purchaseAmount = purchaseAmount.value,
                    isAuthenticated = isAuthenticated.value,
                    selectedCard = selectedCard,
                    customSuccessPage = customSuccessPage
                )
            }
        }

        if (isLoading.value
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
                    isLoading.value = false
                    actionOnLoadingCompleted()
                },
                onResult = {
                    cardRepository.getCards(
                        phone = DataHolder.userPhone,
                        error = {
                            isLoading.value = false
                            actionOnLoadingCompleted()
                        },
                        result = {
                            isLoading.value = false
                            savedCards.value = it
                            actionOnLoadingCompleted()
                            if (it.isNotEmpty()) {
                                selectedCard.value = it[0]
                            }
                        }
                    )
                }
            )
        }
    }
}



package kz.airbapay.apay_android.ui.pages.dialog

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.provideRetrofit
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.CardRepository
import kz.airbapay.apay_android.network.repository.GooglePayRepository
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.network.repository.startAuth
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitErrorState
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingAmount
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.dialog.start_processing_ext.InitViewStartProcessingCards
import kz.airbapay.apay_android.ui.pages.googlepay.GooglePayPage
import kz.airbapay.apay_android.ui.pages.home.bl.startPaymentProcessingGooglePay
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.InitHeader
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

// todo show Dialog only if hase changed
@Composable
internal fun StartProcessingView( //todo rename StartProcessingPage, startprocessingpage. move Dialog in dialog
    actionClose: () -> Unit,
    actionOnLoadingCompleted: () -> Unit = {},
    backgroundColor: Color = ColorsSdk.bgBlock,
    isAuthenticated: MutableState<Boolean>,
    customSuccessPage: @Composable (() -> Unit)? = null,
    navController: NavController?
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val retrofit = provideRetrofit(context)
    val redirectUrl = remember {
        mutableStateOf<String?>(null)
    }

    if (retrofit != null) {
        val api = retrofit.create(Api::class.java)
        val paymentRepository = PaymentsRepository(api)
        val cardRepository = CardRepository(api)
        val authRepository = AuthRepository(api)
        val googlePayRepository = GooglePayRepository(api)

        BackHandler {
            actionClose()
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
                topStart = 0.dp,
                topEnd = 0.dp
            ),
            modifier = Modifier
                .fillMaxSize()
                .onSizeChanged {
                    size.value = it
                },
            elevation = 0.dp
        ) {
            if (!isLoading.value) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {

                    ViewToolbar(
                        title = paymentByCard(),
                        backIcon = R.drawable.ic_arrow_back,
                        actionBack = actionClose
                    )

                    if (isError.value) {
                        InitErrorState()

                    } else {
                        InitViewStartProcessingAmount(purchaseAmount.value)

                        if (redirectUrl.value != null) {
                            GooglePayPage(
                                url = redirectUrl.value,
                                navController = navController
                            )
                        }

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
            }

            if (isLoading.value) {
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
                        startPaymentProcessingGooglePay(
                            redirectUrl = redirectUrl,
                            coroutineScope = coroutineScope,
                            paymentsRepository = paymentRepository,
                            googlePayRepository = googlePayRepository,
                            authRepository = authRepository,
                            navController = navController,

                        )

                        cardRepository.getCards(
                            accountId = DataHolder.accountId,
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

    } else {
        Card(
            backgroundColor = backgroundColor,
            shape = RoundedCornerShape(
                topStart = 0.dp,
                topEnd = 0.dp
            ),
            modifier = Modifier.fillMaxWidth(),
            elevation = 0.dp
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {

                InitHeader(
                    title = paymentByCard(),
                    actionClose = actionClose,
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(ColorsSdk.gray0)
                )

                InitErrorState()
            }
        }
    }
}



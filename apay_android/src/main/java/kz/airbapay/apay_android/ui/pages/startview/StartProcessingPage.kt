package kz.airbapay.apay_android.ui.pages.startview

import android.app.KeyguardManager
import android.content.Context
import android.os.Bundle
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.Dispatchers.IO
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.cvvInfo
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openGooglePay
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.network.repository.startAuth
import kz.airbapay.apay_android.ui.pages.googlepay.GPayView
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.BaseComposeGooglePayActivity
import kz.airbapay.apay_android.ui.pages.startview.bl.fetchMerchantsWithNextStep
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.EnterCvvBottomSheet
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingCards
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.TopInfoView
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

internal class StartProcessingActivity : BaseComposeGooglePayActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            StartProcessingPage(
                actionClose = { this@StartProcessingActivity.finish() }
            )
        }
    }

    override fun onResume() {
        super.onResume()
        DataHolder.isGooglePayFlow = true
    }
}

@Composable
internal fun StartProcessingPage(
    actionClose: () -> Unit,
    backgroundColor: Color = ColorsSdk.bgBlock
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { false },
        skipHalfExpanded = true
    )

    val coroutineScope = rememberCoroutineScope()
    val activity = LocalContext.current as BaseComposeGooglePayActivity
    val keyguardManager = activity.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager

    BackHandler {
        coroutineScope.launch {
            if (sheetState.isVisible) sheetState.hide()
            else actionClose()
        }
    }

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()

    val size = remember { mutableStateOf(IntSize.Zero) }
    val googlePayRedirectUrl = rememberSaveable { mutableStateOf<String?>(null) }
    val isLoading = rememberSaveable { mutableStateOf(true) }
    val isLoadingGooglePay = activity.paymentModel?.isLoading?.collectAsState()

    val selectedCard = rememberSaveable { mutableStateOf<BankCard?>(null) }

    val savedCards = rememberSaveable {
        mutableStateOf<List<BankCard>>(emptyList())
    }

    val selectedIndex = rememberSaveable {
        mutableStateOf(0)
    }

    val cvvFocusRequester = FocusRequester()
    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()

    Scaffold(
        scaffoldState = scaffoldState
    ) { padding ->

        ModalBottomSheetLayout(
            sheetState = sheetState,
            sheetBackgroundColor = ColorsSdk.transparent,
            sheetGesturesEnabled = false,
            sheetContent = {
                EnterCvvBottomSheet(
                    actionClose = {
                        focusManager.clearFocus()
                        coroutineScope.launch {
                            sheetState.hide()
                        }
                    },
                    cardMasked = selectedCard.value?.getMaskedPanClearedWithPoint(),
                    isLoading = isLoading,
                    cardId = selectedCard.value?.id,
                    cvvFocusRequester = cvvFocusRequester,
                    showCvvInfo = {
                        coroutineScope.launch {
                            scaffoldState.snackbarHostState.showSnackbar(
                                message = cvvInfo()
                            )
                        }
                    }
                )
            },
            modifier = Modifier.fillMaxSize()
        ) {
            if (!isLoading.value) {
                ConstraintLayout {
                    val (buttonRef) = createRefs()

                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier
                            .recomposeHighlighter()
                            .background(backgroundColor)
                            .fillMaxSize()
                            .padding(padding)
                            .onSizeChanged {
                                size.value = it
                            }

                    ) {

                        ViewToolbar(
                            title = paymentByCard(),
                            backIcon = R.drawable.ic_arrow_back,
                            actionBack = actionClose
                        )

                        TopInfoView(purchaseAmount.value)

                        if (DataHolder.featureGooglePay
                            && keyguardManager.isKeyguardSecure
                        ) {
                            GPayView(
                                openGooglePayForWebFlow = {
                                    openGooglePay(
                                        redirectUrl = googlePayRedirectUrl.value,
                                        activity = activity
                                    )
                                }
                            )
                        }

                        if (savedCards.value.isNotEmpty()) {
                            InitViewStartProcessingCards(
                                savedCards = savedCards.value,
                                selectedCard = selectedCard,
                                selectedIndex = selectedIndex
                            )
                        }
                    }

                    InitViewStartProcessingButtonNext(
                        isLoading = isLoading,
                        purchaseAmount = purchaseAmount.value,
                        selectedCard = selectedCard,
                        showCvv = {
                            coroutineScope.launch {
                                sheetState.show()
                                cvvFocusRequester.requestFocus()

                                val def = coroutineScope.async(IO) {
                                    Thread.sleep(1000)
                                    isLoading.value = false
                                }
                                def.start()
                            }
                        },
                        modifier = Modifier
                            .recomposeHighlighter()
                            .padding(horizontal = 16.dp)
                            .padding(top = 16.dp)
                            .padding(bottom = 32.dp)
                            .constrainAs(buttonRef) {
                                bottom.linkTo(parent.bottom)
                            }
                    )
                }

                if (isLoading.value || isLoadingGooglePay?.value == true) {
                    ProgressBarView(
                        size = size,
                        modifier = Modifier.wrapContentHeight()
                    )
                }
            }

            LaunchedEffect("CardRepository") {

                launch {

                    startAuth(
                        authRepository = Repository.authRepository!!,
                        onError = {
                            openErrorPageWithCondition(
                                errorCode = ErrorsCode.error_1.code,
                                activity = activity
                            )
                        },
                        onSuccess = {
                            fetchMerchantsWithNextStep(
                                activity = activity,
                                googlePayRedirectUrl = googlePayRedirectUrl,
                                savedCards = savedCards,
                                selectedCard = selectedCard,
                                isLoading = isLoading
                            )
                        }
                    )
                }
            }
        }
    }
}

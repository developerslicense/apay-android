package kz.airbapay.apay_android.ui.pages.startview

import android.app.Activity
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.cvvInfo
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.backToApp
import kz.airbapay.apay_android.data.utils.openGooglePay
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.network.loggly.Logger
import kz.airbapay.apay_android.ui.pages.BaseActivity
import kz.airbapay.apay_android.ui.pages.googlepay.GPayView
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.EnterCvvBottomSheet
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingCards
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.TopInfoView
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

internal class StartProcessingActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DataHolder.needDisableScreenShot) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }

        Logger.log(
            message = "onCreate StartProcessingActivity",
        )

        val airbaPay = AirbaPayBaseGooglePay(this)

        setContent {
            StartProcessingPage(
                airbaPayBaseGooglePay = airbaPay,
                actionClose = { backToApp() }
            )
        }
    }

    override fun getPageName() = this.localClassName
}

@Composable
internal fun StartProcessingPage(
    airbaPayBaseGooglePay: AirbaPayBaseGooglePay,
    actionClose: () -> Unit,
    backgroundColor: Color = ColorsSdk.bgBlock
) {
    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { false },
        skipHalfExpanded = true
    )

    val coroutineScope = rememberCoroutineScope()
    val activity = LocalContext.current as Activity

    BackHandler {
        coroutineScope.launch {
            if (sheetState.isVisible) sheetState.hide()
            else actionClose()
        }
    }

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()
    val purchaseNumber = DataHolder.purchaseNumber.collectAsState()

    val googlePayRedirectUrl = rememberSaveable { mutableStateOf<String?>(null) }
    val isLoading = rememberSaveable { mutableStateOf(true) }
    val isLoadingGooglePay = airbaPayBaseGooglePay.paymentModel?.isLoading?.collectAsState()

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
                    isLoading = { b -> isLoading.value = b },
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
            ConstraintLayout {
                val (buttonRef) = createRefs()

                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier
                        .recomposeHighlighter()
                        .background(backgroundColor)
                        .fillMaxSize()
                        .padding(padding)

                ) {

                    ViewToolbar(
                        title = paymentByCard(),
                        backIcon = R.drawable.ic_arrow_back,
                        actionBack = actionClose
                    )

                    TopInfoView(
                        purchaseAmount = purchaseAmount.value,
                        numberOfPurchase = purchaseNumber.value
                    )

                    GPayView(
                        airbaPayBaseGooglePay = airbaPayBaseGooglePay,
                        openGooglePayForWebFlow = {
                            openGooglePay(
                                redirectUrl = googlePayRedirectUrl.value,
                                activity = activity
                            )
                        }
                    )

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
                ProgressBarView()
            }

            LaunchedEffect("CardRepository") {

                launch {
                    fetchMerchantsWithNextStep(
                        activity = activity,
                        googlePayRedirectUrl = googlePayRedirectUrl,
                        savedCards = savedCards,
                        selectedCard = selectedCard,
                        isLoading = isLoading
                    )
                }
            }
        }
    }
}

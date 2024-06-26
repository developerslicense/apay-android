package kz.airbapay.apay_android.ui.pages.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.view.WindowManager
import androidx.activity.compose.setContent
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetState
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.Scaffold
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.material.rememberScaffoldState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import io.card.payment.CardIOActivity
import io.card.payment.CreditCard
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.SCAN_REQUEST_CODE
import kz.airbapay.apay_android.data.constant.cvvInfo
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.constant.paymentOfPurchase
import kz.airbapay.apay_android.data.constant.saveCardData
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.MaskUtils
import kz.airbapay.apay_android.data.utils.backToApp
import kz.airbapay.apay_android.data.utils.backToStartPage
import kz.airbapay.apay_android.data.utils.card_utils.getCardTypeFromNumber
import kz.airbapay.apay_android.data.utils.openCardScanner
import kz.airbapay.apay_android.data.utils.openGooglePay
import kz.airbapay.apay_android.network.loggly.Logger
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.pages.BaseActivity
import kz.airbapay.apay_android.ui.pages.dialogs.InitDialogExit
import kz.airbapay.apay_android.ui.pages.googlepay.GPayView
import kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay
import kz.airbapay.apay_android.ui.pages.home.bl.checkValid
import kz.airbapay.apay_android.ui.pages.home.bl.startPaymentProcessing
import kz.airbapay.apay_android.ui.pages.home.presentation.BottomImages
import kz.airbapay.apay_android.ui.pages.home.presentation.CardNumberView
import kz.airbapay.apay_android.ui.pages.home.presentation.CvvView
import kz.airbapay.apay_android.ui.pages.home.presentation.DateExpiredView
import kz.airbapay.apay_android.ui.pages.home.presentation.SwitchedView
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.TopInfoView
import kz.airbapay.apay_android.ui.ui_components.ViewButton
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

internal class HomeActivity : BaseActivity() {

    private val cardNumberText = mutableStateOf(TextFieldValue())
    private val paySystemIcon = mutableStateOf<Int?>(null)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        if (DataHolder.needDisableScreenShot) {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_SECURE,
                WindowManager.LayoutParams.FLAG_SECURE
            )
        }
        Logger.log(
            message = "onCreate HomeActivity",
        )

        val airbaPay = AirbaPayBaseGooglePay(this)

        setContent {
            HomePage(
                airbaPayBaseGooglePay = airbaPay,
                cardNumberText = cardNumberText,
                paySystemIcon = paySystemIcon
            )
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == SCAN_REQUEST_CODE
            && data?.hasExtra(CardIOActivity.EXTRA_SCAN_RESULT) == true
        ) {
            val result = data.getParcelableExtra<CreditCard>(CardIOActivity.EXTRA_SCAN_RESULT)
            onResult(result?.cardNumber)
        }
    }

    private fun onResult(cardNumber: String?) {
        val maskUtils = MaskUtils("AAAA AAAA AAAA AAAA")
        val pan = maskUtils.format(cardNumber ?: "")

        cardNumberText.value = TextFieldValue(
            text = pan,
            selection = TextRange(cardNumber?.length ?: 0)
        )

        paySystemIcon.value = getCardTypeFromNumber(pan).icon
    }

    override fun getPageName() = this.localClassName
}

@Composable
internal fun HomePage(
    airbaPayBaseGooglePay: AirbaPayBaseGooglePay,
    cardNumberText: MutableState<TextFieldValue>,
    paySystemIcon: MutableState<Int?>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val activity = LocalContext.current as Activity

    val isLoadingGooglePay = airbaPayBaseGooglePay.paymentModel?.isLoading?.collectAsState()
    val isLoading = remember { mutableStateOf(false) }
    val showDialogExit = remember { mutableStateOf(false) }
    val switchSaveCard = remember { mutableStateOf(false) }

    val cardNumberFocusRequester = FocusRequester()
    val dateExpiredFocusRequester = FocusRequester()
    val cvvFocusRequester = FocusRequester()

    val dateExpiredText = remember { mutableStateOf(TextFieldValue("")) }
    val cvvText = remember { mutableStateOf(TextFieldValue("")) }

    val cardNumberError = remember { mutableStateOf<String?>(null) }
    val dateExpiredError = remember { mutableStateOf<String?>(null) }
    val cvvError = remember { mutableStateOf<String?>(null) }

    val focusManager = LocalFocusManager.current
    val scaffoldState = rememberScaffoldState()

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
    )

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()
    val purchaseNumber = DataHolder.purchaseNumber.collectAsState()

    BackHandler {
        onBackPressed(
            sheetState = sheetState,
            dateExpiredText = dateExpiredText,
            cardNumberText = cardNumberText,
            cvvText = cvvText,
            showDialogExit = showDialogExit,
            activity = activity,
            coroutineScope = coroutineScope
        )
    }

    Scaffold(
        scaffoldState = scaffoldState,
        modifier = Modifier
            .fillMaxSize()
            .pointerInput(Unit) {
                detectTapGestures(
                    onTap = { focusManager.clearFocus(true) }
                )
            }

    ) { padding ->

        ConstraintLayout {
            val buttonRef = createRef()
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ColorsSdk.bgBlock)
            ) {
                ViewToolbar(
                    title = paymentOfPurchase(),
                    backIcon = R.drawable.ic_arrow_back,
                    actionBack = {
                        onBackPressed(
                            sheetState = sheetState,
                            dateExpiredText = dateExpiredText,
                            cardNumberText = cardNumberText,
                            cvvText = cvvText,
                            showDialogExit = showDialogExit,
                            activity = activity,
                            coroutineScope = coroutineScope
                        )
                    }
                )


                TopInfoView(
                    purchaseAmount = purchaseAmount.value,
                    numberOfPurchase = purchaseNumber.value
                )

                if (!DataHolder.hasSavedCards) {
                    GPayView(
                        airbaPayBaseGooglePay = airbaPayBaseGooglePay,
                        openGooglePayForWebFlow = {
                            openGooglePay(
                                redirectUrl = DataHolder.googlePayButtonUrl,
                                activity = activity
                            )
                        }
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))
                CardNumberView(
                    cardNumberText = cardNumberText,
                    cardNumberError = cardNumberError,
                    cardNumberFocusRequester = cardNumberFocusRequester,
                    dateExpiredFocusRequester = dateExpiredFocusRequester,
                    actionClickScanCard = {
                        openCardScanner(activity)
                    },
                    paySystemIcon = paySystemIcon
                )

                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier
                        .padding(start = 16.dp, end = 16.dp)
                        .fillMaxWidth()
                ) {
                    DateExpiredView(
                        dateExpiredFocusRequester = dateExpiredFocusRequester,
                        dateExpiredError = dateExpiredError,
                        dateExpiredText = dateExpiredText,
                        cvvFocusRequester = cvvFocusRequester,
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(end = 6.dp)
                    )
                    CvvView(
                        cvvError = cvvError,
                        cvvFocusRequester = cvvFocusRequester,
                        cvvText = cvvText,
                        actionClickInfo = {
                            coroutineScope.launch {
                                scaffoldState.snackbarHostState.showSnackbar(
                                    message = cvvInfo()
                                )
                            }
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = 6.dp)
                    )
                }


                if (DataHolder.isRenderSavedCards()) {
                    Spacer(modifier = Modifier.height(24.dp))
                    SwitchedView(
                        text = saveCardData(),
                        switchCheckedState = switchSaveCard,
                    )
                }

                BottomImages()

            }

            ViewButton(
                title = "${payAmount()} ${purchaseAmount.value}",
                actionClick = {
                    focusManager.clearFocus(true)

                    val isValid = checkValid(
                        cardNumber = cardNumberText.value.text,
                        cardNumberError = cardNumberError,
                        dateExpired = dateExpiredText.value.text,
                        dateExpiredError = dateExpiredError,
                        cvvError = cvvError,
                        cvv = cvvText.value.text
                    )

                    if (isValid) {
                        isLoading.value = true

                        Repository.cardRepository?.getCardsBank(
                            pan = cardNumberText.value.text.replace(" ", ""),
                            coroutineScope = coroutineScope,
                            next = {
                                startPaymentProcessing(
                                    activity = activity,
                                    isLoading = isLoading,
                                    saveCard = switchSaveCard.value,
                                    cardNumber = cardNumberText.value.text,
                                    cvv = cvvText.value.text,
                                    dateExpired = dateExpiredText.value.text,
                                    coroutineScope = coroutineScope
                                )
                            }
                        )
                    }
                },
                modifierRoot = Modifier
                    .padding(horizontal = 16.dp)
                    .padding(bottom = 16.dp)
                    .constrainAs(buttonRef) {
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )

            if (isLoading.value || isLoadingGooglePay?.value == true) {
                ProgressBarView()
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
}

private fun onBackPressed(
    sheetState: ModalBottomSheetState,
    dateExpiredText: MutableState<TextFieldValue>,
    cardNumberText: MutableState<TextFieldValue>,
    cvvText: MutableState<TextFieldValue>,
    showDialogExit: MutableState<Boolean>,
    activity: Activity,
    coroutineScope: CoroutineScope
) {

    if (sheetState.isVisible) coroutineScope.launch { sheetState.hide() }
    else if (
        dateExpiredText.value.text.isNotBlank()
        || cardNumberText.value.text.isNotBlank()
        || cvvText.value.text.isNotBlank()
    ) {
        showDialogExit.value = true

    } else if (!DataHolder.isRenderSavedCards() || !DataHolder.hasSavedCards) {
        activity.backToApp()

    } else {
        backToStartPage(activity)
    }
}


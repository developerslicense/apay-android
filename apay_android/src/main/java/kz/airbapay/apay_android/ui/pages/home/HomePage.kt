package kz.airbapay.apay_android.ui.pages.home

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material.ModalBottomSheetLayout
import androidx.compose.material.ModalBottomSheetValue
import androidx.compose.material.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.constraintlayout.compose.ConstraintLayout
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.payAmount
import kz.airbapay.apay_android.data.constant.paymentOfPurchase
import kz.airbapay.apay_android.data.constant.saveCardData
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.MaskUtils
import kz.airbapay.apay_android.data.utils.backToStartPage
import kz.airbapay.apay_android.data.utils.card_utils.getCardTypeFromNumber
import kz.airbapay.apay_android.data.utils.openCardScanner
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity
import kz.airbapay.apay_android.ui.pages.dialogs.InitDialogExit
import kz.airbapay.apay_android.ui.pages.home.bl.checkValid
import kz.airbapay.apay_android.ui.pages.home.bl.startPaymentProcessing
import kz.airbapay.apay_android.ui.pages.home.presentation.BottomImages
import kz.airbapay.apay_android.ui.pages.home.presentation.CardNumberView
import kz.airbapay.apay_android.ui.pages.home.presentation.CvvBottomSheet
import kz.airbapay.apay_android.ui.pages.home.presentation.CvvView
import kz.airbapay.apay_android.ui.pages.home.presentation.DateExpiredView
import kz.airbapay.apay_android.ui.pages.home.presentation.SwitchedView
import kz.airbapay.apay_android.ui.pages.home.presentation.TopInfoView
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ViewButton
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

internal class HomeActivity : ComponentActivity() {

    private val cardNumberText = mutableStateOf(TextFieldValue())
    private val paySystemIcon = mutableStateOf<Int?>(null)
    var scanResultLauncher: ActivityResultLauncher<Intent>? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        scanResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            if (result.resultCode == Activity.RESULT_OK) {
                val data = result.data
                val cardNumber = data?.extras?.getString(ScanActivity.RESULT_CARD_NUMBER)
                val maskUtils = MaskUtils("AAAA AAAA AAAA AAAA")
                val pan = maskUtils.format(cardNumber ?: "")

                cardNumberText.value = TextFieldValue(
                    text = pan,
                    selection = TextRange(cardNumber?.length ?: 0)
                )

                paySystemIcon.value = getCardTypeFromNumber(pan).icon
            }
        }

        setContent {
            HomePage(
                cardNumberText = cardNumberText,
                paySystemIcon = paySystemIcon
            )
        }
    }
}

@Composable
internal fun HomePage(
    cardNumberText: MutableState<TextFieldValue>,
    paySystemIcon: MutableState<Int?>,
    coroutineScope: CoroutineScope = rememberCoroutineScope()
) {
    val activity = LocalContext.current as Activity

    val isLoading = remember { mutableStateOf(true) }
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

    val sheetState = rememberModalBottomSheetState(
        initialValue = ModalBottomSheetValue.Hidden,
        confirmValueChange = { it != ModalBottomSheetValue.HalfExpanded },
    )

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()

    BackHandler {
        coroutineScope.launch {
            if (sheetState.isVisible) sheetState.hide()
            else if (
                dateExpiredText.value.text.isNotBlank()
                || cardNumberText.value.text.isNotBlank()
                || cvvText.value.text.isNotBlank()
            ) showDialogExit.value = true
            else backToStartPage(activity)
        }
    }

    ModalBottomSheetLayout(
        sheetState = sheetState,
        sheetBackgroundColor = ColorsSdk.transparent,
        sheetContent = {
            CvvBottomSheet {
                coroutineScope.launch { sheetState.hide() }
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
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
                        if (
                            dateExpiredText.value.text.isNotBlank()
                            || cardNumberText.value.text.isNotBlank()
                            || cvvText.value.text.isNotBlank()
                        ) showDialogExit.value = true
                        else backToStartPage(activity)
                    }
                )


                TopInfoView(purchaseAmount.value)

                Spacer(modifier = Modifier.height(16.dp))
                CardNumberView(
                    cardNumberText = cardNumberText,
                    cardNumberError = cardNumberError,
                    cardNumberFocusRequester = cardNumberFocusRequester,
                    dateExpiredFocusRequester = dateExpiredFocusRequester,
                    actionClickScanCard = {
                        openCardScanner(activity as HomeActivity)
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
                                sheetState.show()
                            }
                        },
                        modifier = Modifier
                            .weight(0.5f)
                            .padding(start = 6.dp)
                    )
                }


                Spacer(modifier = Modifier.height(24.dp))
                SwitchedView(
                    text = saveCardData(),
                    switchCheckedState = switchSaveCard,
                )

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


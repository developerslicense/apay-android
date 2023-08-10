package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.RegexConst
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.constant.needFillTheField
import kz.airbapay.apay_android.data.constant.wrongCardNumber
import kz.airbapay.apay_android.data.constant.wrongCvv
import kz.airbapay.apay_android.data.constant.wrongDate
import kz.airbapay.apay_android.data.constant.wrongEmail
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.model.PaymentEntryRequest
import kz.airbapay.apay_android.data.model.Secure3D
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.card_utils.isDateValid
import kz.airbapay.apay_android.data.utils.card_utils.validateCardNumWithLuhnAlgorithm
import kz.airbapay.apay_android.data.utils.getNumberCleared
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.data.utils.openWebView
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.network.repository.startAuth

private var saveCardSaved = false
private var sendReceiptSaved = false
private var cardSaved: BankCard? = null

internal fun checkValid(
    cardNumber: String?,
    cardNumberError: MutableState<String?>,

    dateExpired: String?,
    dateExpiredError: MutableState<String?>,

    cvv: String?,
    cvvError: MutableState<String?>,

    emailStateSwitched: Boolean,
    email: String?,
    emailError: MutableState<String?>,


    ): Boolean {
    var hasError = false

    if (emailStateSwitched
        && (email.isNullOrBlank() || !email.contains(Regex(RegexConst.emailValidation)))
    ) {
        hasError = true
        emailError.value = wrongEmail()

    } else {
        emailError.value = null
    }

    if (cardNumber.isNullOrBlank()) {
        hasError = true
        cardNumberError.value = needFillTheField()

    } else if (!validateCardNumWithLuhnAlgorithm(cardNumber)) {
        hasError = true
        cardNumberError.value = wrongCardNumber()

    } else {
        cardNumberError.value = null
    }

    if (dateExpired.isNullOrBlank()) {
        hasError = true
        dateExpiredError.value = needFillTheField()

    } else if (!isDateValid(dateExpired)) {
        hasError = true
        dateExpiredError.value = wrongDate()

    } else {
        dateExpiredError.value = null
    }

    if (cvv.isNullOrBlank()) {
        hasError = true
        cvvError.value = needFillTheField()

    } else if (cvv.length < 3) {
        hasError = true
        cvvError.value = wrongCvv()

    } else {
        cvvError.value = null
    }

    return !hasError
}

internal fun startPaymentProcessing(
    navController: NavController,
    isLoading : MutableState<Boolean>,
    saveCard: Boolean = false,
    sendReceipt: Boolean = false,
    cardNumber: String,
    email: String? = null,
    dateExpired: String? = null,
    cvv: String? = null,
    cardId: String? = null,
    authRepository: AuthRepository,
    paymentsRepository: PaymentsRepository,
    coroutineScope: CoroutineScope
) {
    isLoading .value = true

    cardSaved = BankCard(
        pan = getNumberCleared(cardNumber),
        expiry = dateExpired,
        name = "Card Holder",
        cvv = cvv,
        id = cardId
    )
    sendReceiptSaved = sendReceipt
    saveCardSaved = saveCard

    DataHolder.userEmail = email

    coroutineScope.launch {

        startCreatePayment(
            paymentsRepository = paymentsRepository,
            authRepository = authRepository,
            on3DS = { secure3D ->
                openWebView(
                    secure3D = secure3D,
                    navController = navController
                )
            },
            onError = { errorCode ->
                openErrorPageWithCondition(
                    errorCode = errorCode.code,
                    navController = navController
                )
            },
            onSuccess = {
                openSuccess(navController)
            }
        )
    }
}

private fun startCreatePayment(
    on3DS: (secure3D: Secure3D?) -> Unit,
    onSuccess: () -> Unit,
    onError: (ErrorsCode) -> Unit,
    authRepository: AuthRepository,
    paymentsRepository: PaymentsRepository,
) {
    paymentsRepository.createPayment(
        saveCard = saveCardSaved,
        result = {
            startAuth(
                authRepository = authRepository,
                paymentId = it.id,
                onError = { onError(ErrorsCode.error_1) },
                onResult = {
                    val request = PaymentEntryRequest(
                        cardSave = saveCardSaved,
                        email = if (sendReceiptSaved) DataHolder.userEmail else null,
                        sendReceipt = sendReceiptSaved,
                        card = cardSaved!!
                    )
                    paymentsRepository.paymentAccountEntry(
                        param = request,
                        result = { entryResponse ->
                            if (entryResponse.errorCode != "0") {
                                val error = initErrorsCodeByCode(entryResponse.errorCode?.toInt() ?: 1)
                                onError(error)

                            } else if (entryResponse.isSecure3D == true) {
                                on3DS(entryResponse.secure3D)

                            } else {
                                onSuccess()
                            }
                        },
                        error = {
                            onError(ErrorsCode.error_1)
                        }
                    )
                }
            )
        },
        error = {
            onError(ErrorsCode.error_1)
        }
    )
}


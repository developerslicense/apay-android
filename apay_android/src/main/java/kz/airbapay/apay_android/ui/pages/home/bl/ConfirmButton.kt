package kz.airbapay.apay_android.ui.pages.home.bl

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.constant.needFillTheField
import kz.airbapay.apay_android.data.constant.wrongCardNumber
import kz.airbapay.apay_android.data.constant.wrongCvv
import kz.airbapay.apay_android.data.constant.wrongDate
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.model.PaymentEntryRequest
import kz.airbapay.apay_android.data.model.Secure3D
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.card_utils.isDateValid
import kz.airbapay.apay_android.data.utils.card_utils.validateCardNumWithLuhnAlgorithm
import kz.airbapay.apay_android.data.utils.getNumberCleared
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.network.repository.startAuth

internal fun checkValid(
    cardNumber: String?,
    cardNumberError: MutableState<String?>,

    dateExpired: String?,
    dateExpiredError: MutableState<String?>,

    cvv: String?,
    cvvError: MutableState<String?>,

//    emailStateSwitched: Boolean,
//    email: String?,
//    emailError: MutableState<String?>
//
): Boolean {
    var hasError = false

    /* if (emailStateSwitched // todo оставил на всякий случай
         && (email.isNullOrBlank() || !email.contains(Regex(RegexConst.emailValidation)))
     ) {
         hasError = true
         emailError.value = wrongEmail()

     } else {
         emailError.value = null
     }*/

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
    isLoading: MutableState<Boolean>,
    saveCard: Boolean = false,
    cardNumber: String,
    dateExpired: String? = null,
    cvv: String? = null,
    authRepository: AuthRepository,
    paymentsRepository: PaymentsRepository,
    coroutineScope: CoroutineScope
) {
    isLoading.value = true

    val cardSaved = BankCard(
        pan = getNumberCleared(cardNumber),
        expiry = dateExpired,
        name = "Card Holder",
        cvv = cvv,
    )

    coroutineScope.launch {

        startCreatePayment(
            cardSaved = cardSaved,
            saveCardSaved = saveCard,
            paymentsRepository = paymentsRepository,
            authRepository = authRepository,
            on3DS = { secure3D ->
                openAcquiring(
                    redirectUrl = secure3D?.action,
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
    cardSaved: BankCard,
    saveCardSaved: Boolean,
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
                    onSuccessAuth(
                        saveCardSaved = saveCardSaved,
                        cardSaved = cardSaved,
                        paymentsRepository = paymentsRepository,
                        onError = onError,
                        on3DS = on3DS,
                        onSuccess = onSuccess
                    )
                }
            )
        },
        error = {
            onError(ErrorsCode.error_1)
        }
    )
}

private fun onSuccessAuth(
    saveCardSaved: Boolean,
    cardSaved: BankCard,
    paymentsRepository: PaymentsRepository,
    onError: (ErrorsCode) -> Unit,
    on3DS: (secure3D: Secure3D?) -> Unit,
    onSuccess: () -> Unit
) {
    val request = PaymentEntryRequest(
        cardSave = saveCardSaved,
        email = DataHolder.userEmail,
        sendReceipt = DataHolder.userEmail != null,
        card = cardSaved
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


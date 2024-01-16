package kz.airbapay.apay_android.ui.pages.home.bl

import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.constant.needFillTheField
import kz.airbapay.apay_android.data.constant.wrongCardNumber
import kz.airbapay.apay_android.data.constant.wrongCvv
import kz.airbapay.apay_android.data.constant.wrongDate
import kz.airbapay.apay_android.data.utils.card_utils.isDateValid
import kz.airbapay.apay_android.data.utils.card_utils.validateCardNumWithLuhnAlgorithm

internal fun checkValid(
    cardNumber: String?,
    cardNumberError: MutableState<String?>,

    dateExpired: String?,
    dateExpiredError: MutableState<String?>,

    cvv: String?,
    cvvError: MutableState<String?>

): Boolean {
    var hasError = false

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
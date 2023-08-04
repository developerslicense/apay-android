package kz.airbapay.apay_android.ui.pages.home.presentation

import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.constant.RegexConst
import kz.airbapay.apay_android.data.constant.needFillTheField
import kz.airbapay.apay_android.data.constant.wrongCardNumber
import kz.airbapay.apay_android.data.constant.wrongDate
import kz.airbapay.apay_android.data.constant.wrongEmail
import kz.airbapay.apay_android.data.utils.card_utils.isDateValid
import kz.airbapay.apay_android.data.utils.card_utils.validateCardNumWithLuhnAlgorithm

internal fun onPressedConfirm(
    cardNumber: String?,
    cardNumberError: MutableState<String?>,

    dateExpired: String?,
    dateExpiredError: MutableState<String?>,

    emailStateSwitched: Boolean,
    email: String?,
    emailError: MutableState<String?>,


) {
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
}


 /* 




  if (isBlank(readState(context).cvv)) {
    //todo проверку на валидность c бэка?
    hasError = true;
    addState(context, CvvEvent(errorCvv: needFillTheField()));

  } else if ((readState(context).cvv?.length ?? 0) < 3) {
    hasError = true;
    addState(context, CvvEvent(errorCvv: wrongCvv()));

  } else {
    addState(context, const CvvEvent(errorCvv: null));
  }

  if (isBlank(readState(context).dateExpired)) {
    hasError = true;
    addState(context, DateExpiredEvent(errorDateExpired: needFillTheField()));

  } else if (!isDateValid(readState(context).dateExpired)) {
    hasError = true;
    addState(context, DateExpiredEvent(errorDateExpired: wrongDate()));

  } else {
    addState(context, const DateExpiredEvent(errorDateExpired: null));
  }

  if (!hasError) {
    addState(context, const PaymentProcessingEvent(isPaymentProcessing: true));

    BankCard card = BankCard(
      pan: getNumberCleared(readState(context).cardNumber),
      expire: readState(context).dateExpired,
      name: readState(context).nameHolder,
    );

    DataHolder.userEmail = readState(context).email;

    _startProcessing(
        readState(context).saveCardData,
        readState(context).emailState?.switched ?? false,
        card,
        readState(context).cvv,
        (Secure3D? secure3d, bool isRetry) {
          addState(context, const PaymentProcessingEvent(isPaymentProcessing: false));
          Navigator.pushNamed(
            context,
            routesWebView,
            arguments: {
              'action': secure3d?.action,
              'is_retry': isRetry.toString()
            }
          );
        },
        () {
          addState(context, const PaymentProcessingEvent(isPaymentProcessing: false));
          Navigator.pushNamed(
            context,
            routesSuccess,
          );
        },
        (int errorCode, bool isRetry, String? bankName) {
          addState(context, const PaymentProcessingEvent(isPaymentProcessing: false));
          openErrorPageWithCondition(errorCode, context, bankName, isRetry);
        },
    );

  }
}

void _startProcessing(
    bool saveCard,
    bool sendReceipt,
    BankCard card,
    String? cvv,
    void Function(Secure3D? secure3d, bool isRetry) on3DS,
    void Function() onSuccess,
    void Function(int errorCode, bool isRetry, String? bankName) onError,
) async {

  try {
    AuthResponse? authResponseBeforeCreate = await auth(
        paymentId: null,
        user: DataHolder.shopId,
        password: DataHolder.password,
        terminalId: DataHolder.terminalId,
        accessToken: null);
    String? accessToken = authResponseBeforeCreate?.getAccessToken();

    PaymentCreateResponse? result = await createPayment(
      amount: getNumberCleared(DataHolder.purchaseAmount),
      orderNumber: DataHolder.orderNumber,
      invoiceId: DataHolder.invoiceId,
      saveCard: saveCard,
      accessToken: accessToken,
      cardId: null,
      //todo логика насчет сохранненой карты
      goods: DataHolder.goods ?? [],
    );

    AuthResponse? authResponseAfter = await auth(
        paymentId: result?.id(),
        user: DataHolder.shopId,
        password: DataHolder.password,
        terminalId: DataHolder.terminalId,
        accessToken: accessToken);

    PaymentEntryResponse? entryResponse = await paymentAccountEntry(
      accessToken: authResponseAfter?.getAccessToken(),
      saveCard: saveCard,
      sendReceipt: sendReceipt,
      card: card,
      cvv: cvv,
    );

    if (entryResponse == null || entryResponse.errorCode() != 0) {
      onError(entryResponse?.errorCode() ?? ErrorsCode.error_1.code, entryResponse?.isRetry() ?? false, '');

    } else if (entryResponse.isSecure3D() == true) {
      on3DS(entryResponse.secure3D(), entryResponse.isRetry() ?? false);

    } else {
      onSuccess();
    }
  } catch (e) {
    onError(ErrorsCode.error_1.code, false, null);
  }

}*/
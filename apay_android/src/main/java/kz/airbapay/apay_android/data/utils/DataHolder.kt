package kz.airbapay.apay_android.data.utils

import android.app.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kz.airbapay.apay_android.AirbaPaySdk

internal object DataHolder {
  const val sdkVersion: String = "1.1.4"
  var baseUrl = ""

  var connectTimeout = 60
  var receiveTimeout = 60
  var sendTimeout = 60

  var isProd = true
  var enabledLogsForProd = false

  var bankCode: String? = null
  var accessToken: String? = null
  var purchaseAmount: Double = 0.0
  var orderNumber = ""
  var invoiceId = ""
  var shopId = ""
  var userEmail: String? = null
  var accountId = ""
  var userPhone = ""
  var password = ""
  var terminalId  = ""
  const val failureBackUrl = "https://site.kz/failure" // не нужно менять
  const val successBackUrl = "https://site.kz/success"// не нужно менять
  var failureCallback = ""
  var successCallback = ""
  var autoCharge = 0
  var currentLang = AirbaPaySdk.Lang.RU.lang

  var goods: List<AirbaPaySdk.Goods>? = null
  var settlementPayments: List<AirbaPaySdk.SettlementPayment>? = null

  val purchaseAmountFormatted = MutableStateFlow("")

  var frontendCallback: ((activity: Activity, paymentSubmittingResult: Boolean) -> Unit)? = null

  var redirectToCustomSuccessPage: ((Activity) -> Unit)? = null
  var redirectToCustomFinalErrorPage: ((Activity) -> Unit)? = null
  var featureGooglePay: Boolean = false
  var featureSavedCards: Boolean = false

  var googlePayButtonUrl: String? = null
  var isGooglePayFlow: Boolean = true

  var hasSavedCards: Boolean = false
  var isGooglePayNative: Boolean = false
  var hideInternalGooglePayButton: Boolean = false

  var gateway: String? = null
  var gatewayMerchantId: String? = null
  var needDisableScreenShot: Boolean = false
}

object TestAirbaPayStates {
  var shutDownTestFeatureGooglePay: Boolean = false
  var shutDownTestFeatureSavedCards: Boolean = false
}
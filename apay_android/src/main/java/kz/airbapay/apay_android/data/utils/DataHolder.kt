package kz.airbapay.apay_android.data.utils

import android.app.Activity
import kotlinx.coroutines.flow.MutableStateFlow
import kz.airbapay.apay_android.AirbaPaySdk

internal object DataHolder {
  const val sdkVersion: String = "2.0.3"
  var baseUrl = ""

  var connectTimeout = 60
  var receiveTimeout = 60
  var sendTimeout = 60

  var isProd = true
  var enabledLogsForProd = false

  var purchaseAmount: Double = 0.0
  var orderNumber = ""
  var invoiceId = ""

  var bankCode: String? = null
  var token: String? = null // todo rename to authToken

  var userEmail: String? = null
  var userPhone = ""
  var accountId: String = ""

  const val failureBackUrl = "https://site.kz/failure" // не нужно менять
  const val successBackUrl = "https://site.kz/success"// не нужно менять
  var failureCallback = ""
  var successCallback = ""
  var autoCharge = 0
  var currentLang = AirbaPaySdk.Lang.RU.lang

  val purchaseAmountFormatted = MutableStateFlow("")

  var actionOnCloseProcessing: ((activity: Activity, paymentSubmittingResult: Boolean) -> Unit)? = null

  var openCustomPageSuccess: ((activity: Activity) -> Unit)? = null
  var openCustomPageFinalError: ((activity: Activity) -> Unit)? = null

  var gateway: String? = null
  var gatewayMerchantId: String? = null
  var googlePayButtonUrl: String? = null

  var isGooglePayFlow: Boolean = true
  var hasSavedCards: Boolean = false

  var needDisableScreenShot: Boolean = false

  var isGooglePayNative: Boolean = true

  var renderInStandardFlowGooglePay: Boolean = true
  var renderInStandardFlowSavedCards: Boolean = true
  var renderGlobalSecurityCvv: Boolean = true
  var renderGlobalSecurityBiometry: Boolean = true

  var height: Int? = null
  var width: Int? = null
}

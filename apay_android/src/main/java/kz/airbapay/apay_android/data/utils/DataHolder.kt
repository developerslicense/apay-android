package kz.airbapay.apay_android.data.utils

import kz.airbapay.apay_android.AirbaPaySdk

internal object DataHolder {
  var baseUrl = ""

  var connectTimeout = 60
  var receiveTimeout = 60
  var sendTimeout = 60

  var isProd = true

  var accessToken: String? = null
  var purchaseAmount = ""
  var orderNumber = ""
  var invoiceId = ""
  var shopId = ""
  var userEmail: String? = null
  var userPhone = ""
  var password = ""
  var terminalId  = ""
  var failureBackUrl = ""
  var failureCallback = ""
  var successBackUrl = ""
  var successCallback = ""
  var currentLang = AirbaPaySdk.Lang.RU.lang

  var goods: List<AirbaPaySdk.Goods>? = null

}
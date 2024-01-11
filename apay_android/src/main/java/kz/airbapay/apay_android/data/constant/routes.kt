package kz.airbapay.apay_android.data.constant

internal const val ARG_ERROR_CODE = "errorCode"
internal const val ARG_ACTION = "action"
internal const val ARG_CARD_ID = "cardId"
internal const val ARG_IS_GOOGLE_PAY = "isGooglePay"

internal const val ROUTES_START_PROCESSING = "start_processing_page"
internal const val ROUTES_HOME = "home_page"
internal const val ROUTES_SUCCESS = "success"
internal const val ROUTES_REPEAT = "repeat"
internal const val ROUTES_ERROR_FINAL = "error_final"
internal const val ROUTES_ERROR_SOMETHING_WRONG = "error_something_wrong"


internal const val TEMPLATE_ROUTES_ERROR = "error"
internal const val ROUTES_ERROR = "error?$ARG_ERROR_CODE={$ARG_ERROR_CODE}"

internal const val TEMPLATE_ROUTES_ERROR_WITH_INSTRUCTION = "error_with_instruction"
internal const val ROUTES_ERROR_WITH_INSTRUCTION = "error_with_instruction?$ARG_ERROR_CODE={$ARG_ERROR_CODE}"

internal const val TEMPLATE_DEEP_LINK_ACQUIRING = "acquiring_page"
internal const val ROUTES_ACQUIRING = "acquiring_page?$ARG_ACTION={$ARG_ACTION}"

internal const val TEMPLATE_DEEP_LINK_GOOGLE_PAY = "gpay_page"
internal const val ROUTES_GOOGLE_PAY = "gpay_page?$ARG_ACTION={$ARG_ACTION}"




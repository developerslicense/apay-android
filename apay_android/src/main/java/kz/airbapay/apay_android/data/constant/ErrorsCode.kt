package kz.airbapay.apay_android.data.constant

import androidx.navigation.NavController
import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.backToStartPage
import kz.airbapay.apay_android.data.utils.openRepeat

internal enum class ErrorsCode(
    val code: Int,
    val messageRu: String,
    val messageKz: String,
    val descriptionRu: String,
    val descriptionKz: String,
    val buttonTopRu: String,
    val buttonTopKz: String,
    val buttonBottomRu: String,
    val buttonBottomKz: String
) {
    error_1(1, "", "", "", "", "", "", "", ""),
    error_5002(
        5002,
        "Неверный номер карты",
        "Карта нөміріндегі қате",
        tryPayAnotherCardRu,
        tryPayAnotherCardKz,
        payAnotherCardRu,
        payAnotherCardKz,
        goToMarketRu,
        goToMarketKz
    ),
    error_5003(
        5003,
        "Истек срок карты",
        "Картаның мерзімі бітті",
        tryPayAnotherCardRu,
        tryPayAnotherCardKz,
        payAnotherCardRu,
        payAnotherCardKz,
        goToMarketRu,
        goToMarketKz
    ),
    error_5006(
        5006,
        "Неверный CVV",
        "CVV қатесі",
        tryPayAnotherCardRu,
        tryPayAnotherCardKz,
        payAnotherCardRu,
        payAnotherCardKz,
        goToMarketRu,
        goToMarketKz
    ),
    error_5007(
        5007,
        "Недостаточно средств",
        "Қаражат жеткіліксіз",
        "Попробуйте снова или оплатите другой картой",
        "Қайталап көріңіз немесе басқа картамен төлеңіз",
        tryAgainRu,
        tryAgainKz,
        goToMarketRu,
        goToMarketKz
    ),
    error_5008(
        5008,
        limitExceededRu,
        limitExceededKz,
        "Попробуйте оплатить другой картой или измените лимит в настройках банкинга",
        "Басқа картамен төлеуге тырысыңыз немесе банк параметрлерінде лимитті өзгертіңіз",
        tryAgainRu,
        tryAgainKz,
        payAnotherCardRu,
        payAnotherCardKz
    ),
    error_5009(
        5009,
        "Неверно введен код 3ds",
        "3ds коды қате енгізілді",
        "Попробуйте повторно ввести код 3Ds",
        "3D кодын қайта енгізіп көріңіз",
        tryAgainRu,
        tryAgainKz,
        goToMarketRu,
        goToMarketKz
    ),
    error_5020(5020, "", "", "", "", "", "", "", ""),
    error_5999(
        5999,
        limitExceededRu,
        limitExceededKz,
        "Измените лимит в настройках \nбанкинга и попробуйте снова.\nЛибо оплатите другой картой",
        "Банк параметрлерінде шектеуді \nөзгертіп, әрекетті қайталаңыз.\nНемесе басқа картамен төлеңіз",
        tryAgainRu,
        tryAgainKz,
        payAnotherCardRu,
        payAnotherCardKz
    )
}

internal fun initErrorsCodeByCode(code: Int) = when (code) {
    5002 -> ErrorsCode.error_5002
    5003 -> ErrorsCode.error_5003
    5006 -> ErrorsCode.error_5006
    5007 -> ErrorsCode.error_5007
    5008 -> ErrorsCode.error_5008
    5009 -> ErrorsCode.error_5009
    5999 -> ErrorsCode.error_5999
    else -> ErrorsCode.error_1
}


internal fun ErrorsCode.message() = if (DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang) {
    messageKz
} else {
    messageRu
}

internal fun ErrorsCode.description() = if (DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang) {
    descriptionKz
} else {
    descriptionRu
}

internal fun ErrorsCode.buttonTop() = if (DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang) {
    buttonTopKz
} else {
    buttonTopRu
}

internal fun ErrorsCode.buttonBottom() = if (DataHolder.currentLang == AirbaPaySdk.Lang.KZ.lang) {
    buttonBottomKz
} else {
    buttonBottomRu
}


internal fun ErrorsCode.clickOnTop(
    navController: NavController,
    finish: () -> Unit
) {
    when (code) {
        5002 -> { backToStartPage(navController) }
        5003 -> { backToStartPage(navController) }
        5006 -> { backToStartPage(navController) }
        5007 -> { openRepeat(navController) }
        5008 -> { openRepeat(navController) }
        5009 -> { openRepeat(navController) }
        5999 -> { openRepeat(navController) }
        else -> { finish() }
    }
}

internal fun ErrorsCode.clickOnBottom(
    navController: NavController,
    finish: () -> Unit
) {
    when (code) {
        5008 -> { backToStartPage(navController) }
        5999 -> { backToStartPage(navController) }
        else -> { finish() }
    }
}



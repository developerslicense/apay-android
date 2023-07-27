package kz.airbapay.apay_android.ui.resources

enum class ErrorsCode(
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
        "Попробуйте оплатить другой картой",
        "Басқа картамен төлеуге тырысыңыз",
        "Оплатить другой картой",
        "Басқа картамен төлеңіз",
        "Вернуться в магазин",
        "Дүкенге оралу"
    ),
    error_5003(
        5003,
        "Истек срок карты",
        "Картаның мерзімі бітті",
        "Попробуйте оплатить другой картой",
        "Басқа картамен төлеуге тырысыңыз",
        "Оплатить другой картой",
        "Басқа картамен төлеңіз",
        "Вернуться в магазин",
        "Дүкенге оралу"
    ),
    error_5006(
        5006,
        "Неверный CVV",
        "CVV қатесі",
        "Попробуйте оплатить другой картой",
        "Басқа картамен төлеуге тырысыңыз",
        "Оплатить другой картой",
        "Басқа картамен төлеңіз",
        "Вернуться в магазин",
        "Дүкенге оралу"
    ),
    error_5007(
        5007,
        "Недостаточно средств",
        "Қаражат жеткіліксіз",
        "Попробуйте снова или оплатите другой картой",
        "Қайталап көріңіз немесе басқа картамен төлеңіз",
        tryAgainRu,
        tryAgainKz,
        "Вернуться в магазин",
        "Дүкенге оралу"
    ),
    error_5008(
        5008,
        "Превышен лимит \nпо карте",
        "Карта шегінен \nасып кетті",
        "Попробуйте оплатить другой картой или измените лимит в настройках банкинга",
        "Басқа картамен төлеуге тырысыңыз немесе банк параметрлерінде лимитті өзгертіңіз",
        tryAgainRu,
        tryAgainKz,
        "Оплатить другой картой",
        "Басқа картамен төлеңіз"
    ),
    error_5009(
        5009,
        "Неверно введен код 3ds",
        "3ds коды қате енгізілді",
        "Попробуйте повторно ввести код 3Ds",
        "3D кодын қайта енгізіп көріңіз",
        tryAgainRu,
        tryAgainKz,
        "Вернуться в магазин",
        "Дүкенге оралу"
    ),
    error_5020(5020, "", "", "", "", "", "", "", ""),
    error_5999(
        5999,
        "Превышен лимит \nпо карте",
        "Карта шегінен \nасып кетті",
        "Попробуйте оплатить другой картой или измените лимит в настройках банкинга",
        "Басқа картамен төлеуге тырысыңыз немесе банк параметрлерінде лимитті өзгертіңіз",
        tryAgainRu,
        tryAgainKz,
        "Оплатить другой картой",
        "Басқа картамен төлеңіз"
    )
}

fun ErrorsCode.initByCode(code: Int) {
    when (code) {
        5002 -> ErrorsCode.error_5002
        5003 -> ErrorsCode.error_5003
        5006 -> ErrorsCode.error_5006
        5007 -> ErrorsCode.error_5007
        5008 -> ErrorsCode.error_5008
        5009 -> ErrorsCode.error_5009
        5999 -> ErrorsCode.error_5999
        else -> ErrorsCode.error_1
    }
}


fun ErrorsCode.message() = if (StringsBase.currentLang == StringsBase.kz) {
    messageKz
} else {
    messageRu
}

fun ErrorsCode.description() = if (StringsBase.currentLang == StringsBase.kz) {
    descriptionKz
} else {
    descriptionRu
}

fun ErrorsCode.buttonTop() = if (StringsBase.currentLang == StringsBase.kz) {
    buttonTopKz
} else {
    buttonTopRu
}

fun ErrorsCode.buttonBottom() = if (StringsBase.currentLang == StringsBase.kz) {
    buttonBottomKz
} else {
    buttonBottomRu
}


fun clickOnTop(code: Int) {
    when (code) {
//         5002  -> { Navigator.of(context).popUntil((route) => route . isFirst) }
//         5003  -> { Navigator.of(context).popUntil((route) => route . isFirst) }
//         5006  -> { Navigator.of(context).popUntil((route) => route . isFirst) }
//         5007  -> { Navigator.pushNamed(context, routesRepeat) }
//         5008  -> { Navigator.pushNamed(context, routesRepeat) }
//         5009  -> { Navigator.pushNamed(context, routesRepeat) }
//         5999  -> { Navigator.pushNamed(context, routesRepeat) }
        else -> { /*exitSdk()*/
        }
    }
}

fun clickOnBottom(code: Int) {
    when (code) {
//         5008 -> { Navigator.of(context).popUntil((route) => route . isFirst) }
//         5999 ->{ Navigator.of(context).popUntil((route) => route . isFirst) }
        else -> { /*exitSdk()*/
        }

    }
}


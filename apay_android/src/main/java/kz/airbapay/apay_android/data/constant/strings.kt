package kz.airbapay.apay_android.data.constant

import kz.airbapay.apay_android.data.utils.getStrFromRes

// эти исключения из-за того, что в enum используются
internal val tryAgainRu = "Попробовать снова"
internal val tryAgainKz = "Қайтадан байқап көріңіз"

internal val payAnotherCardRu = "Оплатить другой картой"
internal val payAnotherCardKz = "Басқа картамен төлеңіз"

internal val goToMarketRu = "Вернуться в магазин"
internal val goToMarketKz = "Дүкенге оралу"

internal val limitExceededRu = "Превышен лимит \nпо карте"
internal val limitExceededKz = "Карта шегінен \nасып кетті"

internal val tryPayAnotherCardRu = "Попробуйте оплатить другой картой"
internal val tryPayAnotherCardKz = "Басқа картамен төлеуге тырысыңыз"

internal val kzt = "₸"

internal fun paymentByCard() = getStrFromRes("Оплата картой", "Карточка арқылы төлеу")
internal fun paymentByCard2() = getStrFromRes("Оплатить картой", "Картамен төлеу")

internal fun paymentOfPurchase() = getStrFromRes("Оплата покупки", "Сатып алу төлемі")

internal fun amountOfPurchase() = getStrFromRes("Сумма покупки", "Сатып алу сомасы")

internal fun numberOfPurchase() = getStrFromRes("Номер заказа", "Тапсырыс нөмірі")


internal fun holderName() = getStrFromRes("Имя держателя", "Ұстаушы аты")


internal fun cardNumber() = getStrFromRes("Номер карты", "Карта нөмірі")

internal fun dateExpired() = getStrFromRes("ММ/ГГ", "АА/ЖЖ")

internal fun cvv() = getStrFromRes("CVV", "CVV")


internal fun saveCardData() = getStrFromRes("Сохранить данные карты", "Карта мәліметтерін сақтаңыз")

internal fun sendCheckToEmail() =
    getStrFromRes("Отправить чек на e-mail", "Чекті электрондық поштаға жіберіңіз")


internal fun email() = getStrFromRes("E-mail", "E-mail")


internal fun payAmount() = getStrFromRes("Оплатить", "Төлеу")


internal fun cvvInfo() = getStrFromRes(
    "CVV находится на задней стороне вашей платежной карты",
    "CVV төлем картаңыздың артында орналасқан"
)


internal fun cardDataSaved() = getStrFromRes("Данные карты сохранены", "Карта деректері сақталды")


internal fun needFillTheField() = getStrFromRes("Заполните поле", "Өрісті толтырыңыз")


internal fun wrongDate() = getStrFromRes("Неверная дата", "Қате күн")


internal fun wrongEmail() = getStrFromRes("Неверный емейл", "Жарамсыз электрондық пошта")


internal fun wrongCvv() = getStrFromRes("Неверный CVV", "Жарамсыз CVV")


internal fun wrongCardNumber() = getStrFromRes("Неправильный номер карты", "Карта нөмірі қате")


internal fun yes() = getStrFromRes("Да", "Иә")


internal fun no() = getStrFromRes("Нет", "Жоқ")


internal fun dropPayment() = getStrFromRes("Прервать оплату?", "Төлемді тоқтату керек пе?")


internal fun paySuccess() = getStrFromRes("Оплата прошла успешно", "Төлем сәтті болды")


internal fun goToMarker() = getStrFromRes(goToMarketRu, goToMarketKz)


internal fun dropPaymentDescription() = getStrFromRes(
    "Нажимая “Да” введенные карточные данные не будут сохранены",
    "«Иә» түймесін басу арқылы енгізілген карта деректері сақталмайды"
)


internal fun timeForPayExpired() = getStrFromRes("Время оплаты истекло", "Төлеу уақыты аяқталды")


internal fun tryFormedNewCart() = getStrFromRes(
    "Попробуйте сформировать\nкорзину занова",
    "Себетті қайтадан\nжасап көріңіз"
)


internal fun weRepeatYourPayment() = getStrFromRes("Повторяем ваш платеж", "Төлеміңізді қайталаймыз")


internal fun thisNeedSomeTime() = getStrFromRes("Это займет немного времени", "Бұл біраз уақытты алады")


internal fun forChangeLimitInKaspi() = getStrFromRes(
    "Для изменения лимита \nв приложении Kaspi.kz:",
    "Kaspi.kz қолданбасындағы \nшектеуді өзгерту үшін:"
)


internal fun forChangeLimitInHomebank() = getStrFromRes(
    "Для изменения лимита \nв приложении Homebank:",
    "Homebank қолданбасындағы \nшектеуді өзгерту үшін:"
)


internal fun somethingWentWrong() = getStrFromRes("Что-то пошло не так", "Бірдеңе дұрыс болмады")


internal fun somethingWentWrongDescription() = getStrFromRes(
    "Обратитесь в службу поддержки магазина",
    "Дүкен қолдау қызметіне хабарласыңыз"
)

internal fun orPayWithCard() = getStrFromRes(
    "или оплатите картой",
    "немесе картамен төлеңіз"
)

internal fun payAnotherCard() = getStrFromRes(
    payAnotherCardRu,
    payAnotherCardKz
)



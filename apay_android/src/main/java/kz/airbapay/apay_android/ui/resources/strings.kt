package kz.airbapay.apay_android.ui.resources

import getStrFromRes

// эти исключения из-за того, что в enum используются
val tryAgainRu = "Попробовать снова"
val tryAgainKz = "Қайтадан байқап көріңіз"

fun paymentOfPurchase() = getStrFromRes("Оплата покупки", "Сатып алу төлемі")


fun amountOfPurchase() = getStrFromRes("Сумма покупки", "Сатып алу сомасы")

fun numberOfPurchase() = getStrFromRes("Номер заказа", "Тапсырыс нөмірі")


fun holderName() = getStrFromRes("Имя держателя", "Ұстаушы аты")


fun cardNumber() = getStrFromRes("Номер карты", "Карта нөмірі")

fun dateExpired() = getStrFromRes("ММ/ГГ", "АА/ЖЖ")

fun cvv() = getStrFromRes("CVV", "CVV")


fun saveCardData() = getStrFromRes("Сохранить данные карты", "Карта мәліметтерін сақтаңыз")

fun sendCheckToEmail() =
    getStrFromRes("Отправить чек на e-mail", "Чекті электрондық поштаға жіберіңіз")


fun email() = getStrFromRes("E-mail", "E-mail")


fun payAmount() = getStrFromRes("Оплатить", "Төлеу")


fun cvvInfo() = getStrFromRes(
    "CVV находится на задней стороне вашей платежной карты",
    "CVV төлем картаңыздың артында орналасқан"
)


fun cardDataSaved() = getStrFromRes("Данные карты сохранены", "Карта деректері сақталды")


fun needFillTheField() = getStrFromRes("Заполните поле", "Өрісті толтырыңыз")


fun wrongDate() = getStrFromRes("Неверная дата", "Қате күн")


fun wrongEmail() = getStrFromRes("Неверный емейл", "Жарамсыз электрондық пошта")


fun wrongCvv() = getStrFromRes("Неверный CVV", "Жарамсыз CVV")


fun wrongCardNumber() = getStrFromRes("Неправильный номер карты", "Карта нөмірі қате")


fun yes() = getStrFromRes("Да", "Иә")


fun no() = getStrFromRes("Нет", "Жоқ")


fun dropPayment() = getStrFromRes("Прервать оплату?", "Төлемді тоқтату керек пе?")


fun paySuccess() = getStrFromRes("Оплата прошла успешно", "Төлем сәтті болды")


fun goToMarker() = getStrFromRes("Вернуться в магазин", "Дүкенге оралу")


fun dropPaymentDescription() = getStrFromRes(
    "Нажимая “Да” введенные карточные данные не будут сохранены",
    "«Иә» түймесін басу арқылы енгізілген карта деректері сақталмайды"
)


fun timeForPayExpired() = getStrFromRes("Время оплаты истекло", "Төлеу уақыты аяқталды")


fun tryFormedNewCart() = getStrFromRes(
    "Попробуйте сформировать\nкорзину занова",
    "Себетті қайтадан\nжасап көріңіз"
)


fun weRepeatYourPayment() = getStrFromRes("Повторяем ваш платеж", "Төлеміңізді қайталаймыз")


fun thisNeedSomeTime() = getStrFromRes("Это займет немного времени", "Бұл біраз уақытты алады")


fun forChangeLimitInKaspi() = getStrFromRes(
    "Для изменения лимита \nв приложении Kaspi.kz:",
    "Kaspi.kz қолданбасындағы \nшектеуді өзгерту үшін:"
)


fun forChangeLimitInHomebank() = getStrFromRes(
    "Для изменения лимита \nв приложении Homebank:",
    "Homebank қолданбасындағы \nшектеуді өзгерту үшін:"
)


fun somethingWentWrong() = getStrFromRes("Что-то пошло не так", "Бірдеңе дұрыс болмады")


fun somethingWentWrongDescription() = getStrFromRes(
    "Обратитесь в службу поддержки магазина",
    "Дүкен қолдау қызметіне хабарласыңыз"
)


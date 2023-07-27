object StringsBase {
    val ru = "ru"
    val kz = "kz"
    val en = "en"
    val kzt = "₸"

    var currentLang = ru
}

fun getStrFromRes(ru: String, kz: String) = if (StringsBase.currentLang == StringsBase.kz) {
    kz
} else {
    ru
}


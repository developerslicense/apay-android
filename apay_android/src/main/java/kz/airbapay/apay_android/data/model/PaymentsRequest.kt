package kz.airbapay.apay_android.data.model

import kz.airbapay.apay_android.AirbaPaySdk
import kz.airbapay.apay_android.data.utils.getValueFromArguments

internal class SettlementPayment(
    val amount: Int,
    val companyId: String?
)

internal fun initGoodsFromString(arguments: String): AirbaPaySdk.Goods {
    val splitted = arguments.split("?")

    return AirbaPaySdk.Goods(
        brand = getValueFromArguments(splitted, "good_brand").orEmpty(),
        category = getValueFromArguments(splitted, "good_category").orEmpty(),
        model = getValueFromArguments(splitted, "").orEmpty(),
        price = getValueFromArguments(splitted, "good_price")?.toInt() ?: 0,
        quantity = getValueFromArguments(splitted, "good_quantity")?.toInt() ?: 0
    )
}

internal fun AirbaPaySdk.Goods.toMap(): Map<String, Any> {
    return hashMapOf(
        "brand" to brand,
        "category" to category,
//      "discount" to discount,
        "model" to model,
        "price" to price,
        "quantity" to quantity,
    )
}

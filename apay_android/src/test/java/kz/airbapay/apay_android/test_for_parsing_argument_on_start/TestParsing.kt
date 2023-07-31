package kz.airbapay.apay_android.test_for_parsing_argument_on_start

import kz.airbapay.apay_android.data.model.initGoodsFromString
import kz.airbapay.apay_android.data.utils.getValueFromArguments
import kz.airbapay.apay_android.isAssert
import org.junit.Test

class TestParsing {
  @Test
  fun testParsing() {
    val arguments = initArguments()
    val resultArgument = arguments.split("?")
    val resultArgumentProducts = arguments.split("good_model")

    isAssert(getValue(resultArgument, "isProd"), "false")
    isAssert(getValue(resultArgument, "purchaseAmount"), "50150")
    isAssert(getValue(resultArgument, "phone"), "77051234567")
    isAssert(getValue(resultArgument, "orderNumber"), "123")
    isAssert(getValue(resultArgument, "invoiceId"), "123")
    isAssert(getValue(resultArgument, "shopId"), "test-merchant")
    isAssert(getValue(resultArgument, "password"), "123456")
    isAssert(getValue(resultArgument, "terminalId"), "64216e7ccc4a48db060dd689")
    isAssert(getValue(resultArgument, "failureBackUrl"), "https://site.kz/failure")
    isAssert(getValue(resultArgument, "failureCallback"), "https://site.kz/failure-clb")
    isAssert(getValue(resultArgument, "successBackUrl"), "https://site.kz/success")
    isAssert(getValue(resultArgument, "successCallback"), "https://site.kz/success-clb")
    isAssert(getValue(resultArgument, "userEmail"), "test@test.com")

    isAssert(
      resultArgumentProducts[0],
      "isProd=false?" +
              "purchaseAmount=50150?" +
              "phone=77051234567?" +
              "invoiceId=123?" +
              "orderNumber=123?" +
              "shopId=test-merchant?" +
              "password=123456?" +
              "terminalId=64216e7ccc4a48db060dd689?" +
              "failureBackUrl=https://site.kz/failure?" +
              "failureCallback=https://site.kz/failure-clb?" +
              "successBackUrl=https://site.kz/success?" +
              "successCallback=https://site.kz/success-clb?" +
              "userEmail=test@test.com?"
    )

    isAssert(
      resultArgumentProducts[1],
      "=Чай Tess Banana Split черный 20 пирамидок?good_quantity=1?good_brand=Tess?good_price=1000?good_category=Черный чай?"
    )

    isAssert(
      resultArgumentProducts[2],
      "=Чай Standard?good_quantity=2?good_brand=Greenfild?good_price=2000?good_category=Зеленый чай?"
    )

    val good1 = initGoodsFromString(resultArgumentProducts[1])
    val good2 = initGoodsFromString(resultArgumentProducts[2])

    isAssert(good1.category, "Черный чай")
    isAssert(good1.model, "Чай Tess Banana Split черный 20 пирамидок")
    isAssert(good1.price, 1000)
    isAssert(good1.quantity, 1)
    isAssert(good1.brand, "Tess")

    isAssert(good2.category, "Зеленый чай")
    isAssert(good2.model, "Чай Standard")
    isAssert(good2.price, 2000)
    isAssert(good2.quantity, 2)
    isAssert(good2.brand, "Greenfild")
  }

  private fun getValue(
    resultArgument: List<String>,
    key: String
  ) = getValueFromArguments(resultArgument, key)

  private fun initArguments(): String {
    return "isProd=false" +
            "?" +
            "purchaseAmount=50150" +
            "?" +
            "phone=77051234567" +
            "?" +
            "invoiceId=123" +
            "?" +
            "orderNumber=123" +
            "?" +
            "shopId=test-merchant" +
            "?" +
            "password=123456" +
            "?" +
            "terminalId=64216e7ccc4a48db060dd689" +
            "?" +
            "failureBackUrl=https://site.kz/failure" +
            "?" +
            "failureCallback=https://site.kz/failure-clb" +
            "?" +
            "successBackUrl=https://site.kz/success" +
            "?" +
            "successCallback=https://site.kz/success-clb" +
            "?" +
            "userEmail=test@test.com" +
            "?" +
            "good_model=Чай Tess Banana Split черный 20 пирамидок" +
            "?" +
            "good_quantity=1" +
            "?" +
            "good_brand=Tess" +
            "?" +
            "good_price=1000" +
            "?" +
            "good_category=Черный чай" +
            "?" +
            "good_model=Чай Standard" +
            "?" +
            "good_quantity=2" +
            "?" +
            "good_brand=Greenfild" +
            "?" +
            "good_price=2000" +
            "?" +
            "good_category=Зеленый чай" +
            "?"
  }
}

## Техническая документация для интеграции sdk AirbaPay в мобильные приложения


## 1.1 Подключение sdk


## 1.2 Вызов стартовой формы


## 1.3 Пример использования


## 1.4 Подключение нативного GooglePay


# Описание изменений Android



## 1.1  Подключение sdk

Последняя версия 1.0.33 (для jetpackCompose 2024.01.00) и 1.0.33_23_08_00 (для jetpackCompose 23.08.00)

В  ```build.gradle ``` модуля добавь ``` implementation 'kz.airbapay:apay_android:~'```


Для инициализации sdk нужно выполнить ```AirbaPaySdk.initOnCreate()``` до перехода на страницу, где будет использоваться sdk)


| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| shopId | String | да | ID магазина в системе AirbaPay |
| context | Context | да | Контекст приложения |
| password | String | да | Пароль в системе AirbaPay |
| terminalId | String | да | ID терминала под которым создали платеж |
| lang | AirbaPaySdk.Lang | да | Код языка для UI |
| isProd | Boolean | да | Продовская или тестовая среда airbapay |
| phone | String | да | Телефон пользователя |
| failureCallback | String | да | URL вебхука при ошибке |
| successCallback | String | да | URL вебхука при успехе |
| userEmail | String | да | Емейл пользователя, куда будет отправлена квитанция. В случае отсутствия емейла |
| colorBrandMain | androidx.compose.ui.graphics.Color | нет |Брендовый цвет кнопок, переключателей и текста |
| colorBrandInversion | androidx.compose.ui.graphics.Color | нет |Цвет текста у кнопок с брендовым цветом |
| autoCharge | Int | нет | Автоматическое подтверждение при 2х-стадийном режиме 0 - нет, 1 - да |
| enabledLogsForProd | Boolean | нет | Флаг для включения логов |


При смене значения isProd, требуется выгрузить приложение из памяти.



Пример:

```

AirbaPaySdk.initOnCreate(
    context = this.application,
    shopId = "test-merchant",
    password = "123456",
    terminalId = "64216e7ccc4a48db060dd689",
    lang = AirbaPaySdk.Lang.RU,
    isProd = false, 
    phone = "77051000000",
    failureCallback = "https://site.kz/failure-clb", 
    successCallback = "https://site.kz/success-clb",                
    userEmail = "test@test.com", 
    colorBrandMain = Color.Red 
)

```

Перед открытием формы AirbaPay нужно выполнить AirbaPaySdk.initProcessing()

| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| purchaseAmount | Long | да | Сумма платежа |
| invoiceId | String | да | ID платежа в системе магазина | 
| orderNumber | String | да | Номер заказа в системе магазина |
| goods | List<AirbaPaySdk.Goods> | да | Список продуктов для оплаты |
| settlementPayments | List<AirbaPaySdk.SettlementPayment> | нет | Распределение платежа по компаниям. В случае одной компании, может быть null |
| onProcessingResult | (Activity, Boolean) -> Unit | да | Лямбда, вызываемая при клике на кнопку "Вернуться в магазин" и при отмене процесса. Разработчику в ней нужно прописать код для возврата в приложение |


Пример:


```
 private fun initProcessing(
      invoiceId: String,
      orderNumber: String
 ) {
       val goods = listOf(
           AirbaPaySdk.Goods(
               model = "Чай Tess Banana Split черный 20 пирамидок",
               brand = "Tess",
               category = "Черный чай",
               quantity = 1,
               price = 1000
           )
       )

       val settlementPayment = listOf(
           AirbaPaySdk.SettlementPayment(
               amount = 1000,
               companyId = "test_id"
           )
       )

 
       AirbaPaySdk.initProcessing(
           purchaseAmount = 1000,
           invoiceId = invoiceId,
           orderNumber = orderNumber,
           goods = goods,
           settlementPayments = settlementPayment,
           onProcessingReult = { activity, isSuccess ->

               if (isSuccess) { 
                  startActivity(Intent(activity, SuccessActivity::class.java)) 
                } else {
                  startActivity(Intent(activity, ErrorActivity::class.java)) 
                }
        }
     )
   }
}

 ```



## 1.2 Вызов стартовой формы


Открытие формы AirbaPay выполняется через AirbaPaySdk.startAirbaPay().


| Параметр | Тип | Обязательный | Описание |
|----------|-----|--------------|----------|
| activity | Activity | да | Activity |
| redirectToCustomSuccessPage | (Activity) -> Unit | нет | Лямбда, вызываемая для перехода на кастомную страницу успешного завершения. Разработчику в ней нужно прописать код для перехода на кастомную страницу |
| redirectToCustomFinalErrorPage | (Activity) -> Unit | нет | Лямбда, вызываемая для перехода на кастомную страницу Финальной ошибки (остальные ошибки показываются в дефолтной странице ошибки). Разработчику в ней нужно прописать код для перехода на кастомную страницу |


Пример без кастомной страницы:


 ```
startAirbaPay(activity = this@MainActivity)
 ```


## Пример с кастомной станицей:

```
startAirbaPay(
    activity = this@MainActivity,
    redirectToCustomSuccessPage = { 
        val intent = Intent(activity, CustomSuccessActivity::java.class)
        activity.startActivity()
        activity.finish()
    }
)
 ```


Для проекта без Jetpack Compose  потребуются дополнительные настройки проекта.


Добавить в build.gradle app и, в случае модульной архитектуры, в модуль, где будет использоваться:

 ```
android {
 
    buildFeatures {
       compose true
       viewBinding true
    }
    
    composeOptions {
       kotlinCompilerExtensionVersion '1.5.9'
    }
}
 ```

В зависимости от версии котлина, потребуется подобрать версию kotlinCompilerExtensionVersion Compose to Kotlin Compatibility Map  |  Android Developers

 ``` 

dependencies {

    implementation "kz.airbapay:apay_android:$apay_version"
    implementation platform("androidx.compose:compose-bom:2023.08.00")
    implementation 'androidx.compose.ui:ui-graphics'
    implementation 'androidx.compose.ui:ui'

}
 ```




## 1.3 Пример использования


В clickListener для XML или в onClick для Button в композе вставить

 ```
 AirbaPaySdk.initOnCreate(~)
 AirbaPaySdk.initProcessing(~)

 AirbaPaySdk.startAirbaPay(activity = this@MainActivity)
  ```
 

## 1.4 Подключение нативного GooglePay


* Изменить параметр isGooglePayNative в initOnCreate на true

* Перейти в консоль GooglePay  https://pay.google.com/business/console/ и перейти в пункт меню Google Pay API

* Найти в списке “Integrate with your Android app” ваше приложение и кликнуть “Управление”

* Выберите тип интеграции “Через шлюз”.

* Загрузите скриншоты по списку указанному ниже и нажмите “Сохранить”

* Кликните “Submit for approval”

* В случае отказа по каким-либо причинам (к примеру, дизайн кнопки не соответствует их требованиям), нужно будет ответить на письмо гугла, что вы используете стороннее решение компании Airba Pay


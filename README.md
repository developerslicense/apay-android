## Техническая документация для интеграции sdk AirbaPay в мобильные приложения

## 1.1 Подключение sdk

## 1.2 Вызов стартовой формы

## 1.3 Подключение нативного GooglePay

## 1.4 Подключение API внешнего взаимодействия с GooglePay

## 1.5 Рекомендация в случае интеграции в flutter



## 1.1  Подключение sdk

В  ```build.gradle ``` модуля добавь ``` implementation 'kz.airbapay:apay_android:~'```

Для инициализации sdk нужно выполнить ```AirbaPaySdk.initSdk()``` перед
вызовом ```AirbaPaySdk.startAirbaPay() ```.

| Параметр                    | Тип                                 | Обязательный | Описание                                                                                                                                                                                             |
|-----------------------------|-------------------------------------|--------------|------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| shopId                      | String                              | да           | ID магазина в системе AirbaPay                                                                                                                                                                       |
| context                     | Context                             | да           | Контекст приложения                                                                                                                                                                                  |
| password                    | String                              | да           | Пароль в системе AirbaPay                                                                                                                                                                            |
| terminalId                  | String                              | да           | ID терминала под которым создали платеж                                                                                                                                                              |
| lang                        | AirbaPaySdk.Lang                    | да           | Код языка для UI                                                                                                                                                                                     |
| isProd                      | Boolean                             | да           | Продовская или тестовая среда airbapay                                                                                                                                                               |
| phone                       | String                              | да           | Телефон пользователя                                                                                                                                                                                 |
| failureCallback             | String                              | да           | URL вебхука при ошибке                                                                                                                                                                               |
| successCallback             | String                              | да           | URL вебхука при успехе                                                                                                                                                                               |
| userEmail                   | String                              | да           | Емейл пользователя, куда будет отправлена квитанция. В случае отсутствия емейла                                                                                                                      |
| colorBrandMain              | androidx.compose.ui.graphics.Color  | нет          | Брендовый цвет кнопок, переключателей и текста                                                                                                                                                       |
| colorBrandInversion         | androidx.compose.ui.graphics.Color  | нет          | Цвет текста у кнопок с брендовым цветом                                                                                                                                                              |
| autoCharge                  | Int                                 | нет          | Автоматическое подтверждение при 2х-стадийном режиме 0 - нет, 1 - да                                                                                                                                 |
| enabledLogsForProd          | Boolean                             | нет          | Флаг для включения логов                                                                                                                                                                             |
| purchaseAmount              | Long                                | да           | Сумма платежа                                                                                                                                                                                        |
| invoiceId                   | String                              | да           | ID платежа в системе магазина                                                                                                                                                                        | 
| orderNumber                 | String                              | да           | Номер заказа в системе магазина                                                                                                                                                                      |
| goods                       | List<AirbaPaySdk.Goods>             | да           | Список продуктов для оплаты                                                                                                                                                                          |
| settlementPayments          | List<AirbaPaySdk.SettlementPayment> | нет          | Распределение платежа по компаниям. В случае одной компании, может быть null                                                                                                                         |
| onProcessingResult          | (Activity, Boolean) -> Unit         | да           | Лямбда, вызываемая при клике на кнопку "Вернуться в магазин" и при отмене процесса. Разработчику в ней нужно прописать код для возврата в приложение. Обязательно добавить в конце activity.finish() |
| hideInternalGooglePayButton | Boolean                             | нет          | Флаг, определяющий, нужно ли скрывать кнопку гуглпэя на страницах сдк                                                                                                                                |

При смене значения isProd, требуется выгрузить приложение из памяти.

Пример:

```
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
       
AirbaPaySdk.initSdk(
    context = this.application,
    shopId = "test-baykanat",
    password = "baykanat123!",
    terminalId = "65c5df69e8037f1b451a0594",
    lang = AirbaPaySdk.Lang.RU,
    isProd = false, 
    phone = "77051000000",
    failureCallback = "https://site.kz/failure-clb", 
    successCallback = "https://site.kz/success-clb",                
    userEmail = "test@test.com", 
    colorBrandMain = Color.Red,
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
         activity.finish()
    } 
)

```

## 1.2 Вызов стартовой формы

Открытие формы AirbaPay выполняется через  ```AirbaPaySdk.startAirbaPay() ```.

| Параметр                       | Тип                | Обязательный | Описание                                                                                                                                                                                                      |
|--------------------------------|--------------------|--------------|---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| activity                       | Activity           | да           | Activity                                                                                                                                                                                                      |
| redirectToCustomSuccessPage    | (Activity) -> Unit | нет          | Лямбда, вызываемая для перехода на кастомную страницу успешного завершения. Разработчику в ней нужно прописать код для перехода на кастомную страницу                                                         |
| redirectToCustomFinalErrorPage | (Activity) -> Unit | нет          | Лямбда, вызываемая для перехода на кастомную страницу Финальной ошибки (остальные ошибки показываются в дефолтной странице ошибки). Разработчику в ней нужно прописать код для перехода на кастомную страницу |

## Пример без кастомной страницы:

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

Для проекта без Jetpack Compose потребуются дополнительные настройки проекта.

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
    
    kotlinOptions {
      jvmTarget = 17
    }
}
 ```

В зависимости от версии котлина, потребуется подобрать версию kotlinCompilerExtensionVersion
https://developer.android.com/jetpack/androidx/releases/compose-kotlin

 ``` 

dependencies {

    implementation "kz.airbapay:apay_android:$apay_version"
    implementation platform("androidx.compose:compose-bom:2024.01.00")
    implementation 'androidx.compose.ui:ui-graphics'
    implementation 'androidx.compose.ui:ui'

}
 ```

## 1.3 Подключение нативного GooglePay

1) Изменить параметр isGooglePayNative в initSdk на true

2) Перейти в консоль GooglePay  https://pay.google.com/business/console/ и перейти в пункт меню
   Google Pay API

3) Найти в списке “Integrate with your Android app” ваше приложение и кликнуть “Управление”

4) Выберите тип интеграции “Через шлюз”.

5) Загрузите скриншоты по списку указанному ниже и нажмите “Сохранить”

6) Кликните “Submit for approval”

7) В случае отказа по каким-либо причинам (к примеру, дизайн кнопки не соответствует их
   требованиям), нужно будет ответить на письмо гугла, что вы используете стороннее решение компании
   Airba Pay

## 1.4 Подключение API внешнего взаимодействия с GooglePay

1) Нужно, чтоб активити наследовалось от  ```ComponentActivity``` или ```AppCompatActivity```.

2) Перед рендерингом страницы вызвать ```AirbaPaySdk.initSdk(~)```

3) Создать экземпляр ```val airbaPay = AirbaPayBaseGooglePay(appCompatActivity или componentActivity)```
   и вызвать ```airbaPay.authGooglePay(~)```


| Параметр  | Тип        | Обязательный | Описание                                   |
|-----------|------------|--------------|--------------------------------------------|
| onSuccess | () -> Unit | да           | Коллбэк для успешной авторизации в гуглпэй |
| onFailed  | () -> Unit | да           | Коллбэк для ошибки авторизации в гуглпэй   |


Для верстки используется ```AirbaPayGooglePayNativeView(~)```

| Параметр              | Тип                                                                        | Обязательный | Описание                                                      |
|-----------------------|----------------------------------------------------------------------------|--------------|---------------------------------------------------------------|
| airbaPayBaseGooglePay | kz.airbapay.apay_android.ui.pages.googlepay.nativegp.AirbaPayBaseGooglePay | да           | Класс для работы с гугл пэй                                   |
| modifier              | Modifier                                                                   | нет          | Modifier для кнопки                                           |
| buttonTheme           | Int                                                                        | нет          | Темная или светлая тема кнопки. Параметры ниже, в ButtonTheme |
| buttonType            | Int                                                                        | нет          | Оформление кнопки. Параметры ниже, в ButtonType               |
| cornerRadius          | Int                                                                        | нет          | Радиус скругления кнопки                                      |

Параметры кнопки GooglePay

```
ButtonTheme {
    int DARK = 1;
    int LIGHT = 2;
}

ButtonType {
    int BUY = 1;
    int BOOK = 2;
    int CHECKOUT = 3;
    int DONATE = 4;
    int ORDER = 5;
    int PAY = 6;
    int SUBSCRIBE = 7;
    int PLAIN = 8;
}
```

# JetpackCompose 

1) Нужно, чтоб активити наследовал ```ComponentActivity``` или ```AppCompatActivity``` 
в случае фрагментов/активити, где частично используется JetpackCompose. В этом случае надо убедиться, 
что ваш активити использует ```@style/Theme.AppCompat```

2) Вставьте в верстку страницы с указанными выше параметрами

```
 AirbaPayGooglePayNativeView(
                    airbaPayBaseGooglePay = airbaPay,
                    buttonTheme = 1,
                    buttonType = 8,
                    cornerRadius = 8,
                    modifier: Modifier.~
                )
 ```

# Xml:

1) Нужно, чтоб активити наследовал ```AppCompatActivity```, и убедиться,
   что ваш активити использует ```@style/Theme.AppCompat```

2) Добавить в верстку 

``` 
 <androidx.compose.ui.platform.ComposeView
        android:id="@+id/googlePay"
        android:layout_width="match_parent"
        android:layout_height="wrap_content"
        android:paddingStart="~"
        android:paddingTop="~"
        android:paddingEnd="~"
        ~~~~~ />
```

3) Добавить инициализацию элемента

```
binding.googlePay?.apply {
            setContent {
                AirbaPayGooglePayNativeView(
                    airbaPayBaseGooglePay = airbaPay,
                    buttonTheme = 1,
                    buttonType = 8,
                    cornerRadius = 8
                )
            }
        }
```

## 1.5 Рекомендация в случае интеграции в flutter

В dart добавьте:

```
  final MethodChannel channel = MethodChannel("com.example.testFlutter/AirbaPayChannel");

  Future<void> callNativeMethod() async {
    try {
      await channel.invokeMethod('pay');
    } catch (e) {
      print('Error calling native method: $e');
    }
  }
```

И нужно вызвать ```callNativeMethod``` для перехода на страницы сдк.

Нужно создать дополнительный промежуточный активити FlutterAirbaPayActivity,
в котором будет производиться инициализация и переход на страницу сдк.
Это нужно, чтоб при нажатии назад из сдк, не закрывалось приложение.

Добавить в манифест описание этого активити

 ```
<activity android:name=".pay.airba.FlutterAirbaPayActivity"
    android:theme="@style/AppTheme.AppCompat"
    android:exported="false" />
 ```

В ```MainActivity``` нужно добавить

```
 override fun configureFlutterEngine(flutterEngine: FlutterEngine) {
        MethodChannel(flutterEngine.dartExecutor.binaryMessenger, "com.example.testFlutter/AirbaPayChannel")
            .setMethodCallHandler { call: MethodCall, result: Result ->
                if (call.method == "pay") {
                    result.success(null)
                    FlutterAirbaPayActivity.start(this, call, result)
                } else {
                    result.notImplemented()
                }
            }
    }
```

В ```FlutterAirbaPayActivity```

```

class FlutterAirbaPayActivity : AppCompatActivity() {
    /**
     * Два состояния для activity:
     *
     * 1. started airba pay flow
     * 2. finished airba pay flow
     */
    private var isFlowStarted = false

    companion object {
        fun start(context: Activity, call: MethodCall, result: MethodChannel.Result) {
            val intent = Intent(context, FlutterAirbaPayActivity::class.java)
            context.startActivityForResult(intent, 123)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val someInvoiceId = ~
        val someOrderNumber = ~

        val goods = listOf(
            AirbaPaySdk.Goods(
                model = "description",
                brand = "TechnoFit",
                category = "Services",
                quantity = 1,
                price = 1500L,
            ),
        )
        AirbaPaySdk.initSdk(
            context = this,
            isProd = true,
            phone = "+77051234567",
            shopId = "test-baykanat",
            lang = AirbaPaySdk.Lang.RU,
            password = "baykanat123!",
            terminalId = "65c5df69e8037f1b451a0594",
            failureCallback = "https://site.kz/failure-clb",
            successCallback = "https://site.kz/success-clb",
            userEmail = "test@test.com",
            accountId = "1000000000",
            purchaseAmount = 1500L,
            invoiceId = someInvoiceId.toString(),
            orderNumber = someOrderId.toString(),
            goods = goods,
        ) { activity, paymentSubmittingResult ->
            close(paymentSubmittingResult, activity)
        }

        AirbaPaySdk.startAirbaPay(
            this,
            redirectToCustomSuccessPage = { activity ->
                close(true, activity)
            },
        )
    }

    /**
     * Finishes this activity with setting result
     *
     * @see handleAirbaActivityResult
     */
    private fun close(result: Boolean, childActivity: Activity? = null) {
        setResult(
            Activity.RESULT_OK,
            Intent().putExtra("result", result)
        )
        finish()
        childActivity?.finish()
    }

    override fun onStart() {
        super.onStart()
        if(!isFlowStarted) {
            // 1. started airba pay flow (invoked after onCreate)
            isFlowStarted = true
        } else {
            // 2. finished airba pay flow (invoked after backButton pressed or etc)
            close(false)
        }
    }
}
```

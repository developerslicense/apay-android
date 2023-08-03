package kz.airbapay.apay_android

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.ClientConnector
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.CardRepository
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.ui.pages.home.HomePage

class AirbaPayActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clientConnector = ClientConnector(this)
        val api = clientConnector.retrofit.create(Api::class.java)

        val authRepository = AuthRepository(api)
        val cardRepository = CardRepository(api)
        val paymentsRepository = PaymentsRepository(api)

        val errorCode = initErrorsCodeByCode(5002)

        setContent {
            HomePage()
        }
    }
}


package kz.airbapay.apay_android.network.repository

import android.content.Context
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.api.LogglyApi
import kz.airbapay.apay_android.network.base.provideRetrofit
import kz.airbapay.apay_android.network.base.provideRetrofitLoggly
import kz.airbapay.apay_android.network.loggly.LogglyRepository

internal object Repository {
    var authRepository: AuthRepository? = null
    var cardRepository: CardRepository? = null
    var merchantRepository: MerchantsRepository? = null
    var googlePayRepository: GooglePayRepository? = null
    var paymentsRepository: PaymentsRepository? = null
    var createPaymentsRepository: CreatePaymentRepository? = null

    var logglyRepository: LogglyRepository? = null

    fun initRepositories(
        context: Context
    ) {
        val retrofit = provideRetrofit(context)
        val api = retrofit!!.create(Api::class.java)

        val retrofitLoggly = provideRetrofitLoggly(context)
        val logglyApi = retrofitLoggly!!.create(LogglyApi::class.java)

        authRepository = AuthRepository(api)
        cardRepository = CardRepository(api)
        googlePayRepository = GooglePayRepository(api)
        paymentsRepository = PaymentsRepository(api)
        createPaymentsRepository = CreatePaymentRepository(api)
        merchantRepository = MerchantsRepository(api)

        logglyRepository = LogglyRepository(logglyApi)
    }
}
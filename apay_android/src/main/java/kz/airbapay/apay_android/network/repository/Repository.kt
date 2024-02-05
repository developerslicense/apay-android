package kz.airbapay.apay_android.network.repository

import android.content.Context
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.provideRetrofit

internal object Repository {
    var authRepository: AuthRepository? = null
    var cardRepository: CardRepository? = null
    var merchantRepository: MerchantsRepository? = null
    var googlePayRepository: GooglePayRepository? = null
    var paymentsRepository: PaymentsRepository? = null

    fun initRepositories(
        context: Context
    ) {
        val retrofit = provideRetrofit(context)
        val api = retrofit!!.create(Api::class.java)

        authRepository = AuthRepository(api)
        cardRepository = CardRepository(api)
        googlePayRepository = GooglePayRepository(api)
        paymentsRepository = PaymentsRepository(api)
        merchantRepository = MerchantsRepository(api)
    }
}
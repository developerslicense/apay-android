package kz.airbapay.apay_android.network.api

import kz.airbapay.apay_android.data.model.AuthRequest
import kz.airbapay.apay_android.data.model.AuthResponse
import kz.airbapay.apay_android.data.model.CardAddRequest
import kz.airbapay.apay_android.data.model.CardAddResponse
import kz.airbapay.apay_android.data.model.CardsGetResponse
import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.model.PaymentEntryRequest
import kz.airbapay.apay_android.data.model.PaymentEntryResponse
import kz.airbapay.apay_android.data.model.PaymentInfoResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

internal interface Api {

    // auth
    @POST("api/v1/auth/sign-in")
    suspend fun auth(@Body param: AuthRequest): Response<AuthResponse>

    // cards

    @POST("api/v1/cards")
    suspend fun cardAdd(@Body param: CardAddRequest): Response<CardAddResponse>

    @GET("api/v1/cards/{phone}")
    suspend fun getCards(@Path("phone") phone: String): Response<CardsGetResponse>

    @DELETE("api/v1/cards/{id}")
    suspend fun deleteCard(@Path("id") cardId: String): Response<Any>

    // payments

    @POST("api/v1/payments")
    suspend fun createPayment(@Body param: HashMap<String, Any?>): Response<PaymentCreateResponse>

    @GET("api/v1/payments")
    suspend fun getPaymentInfo(): Response<PaymentInfoResponse>

    @PUT("api/v1/payments") // проводка платежа
    suspend fun paymentAccountEntry(param: PaymentEntryRequest): Response<PaymentEntryResponse>

    @PUT("api/v1/payments/retry") // проводка без ввода данных карты
    suspend fun paymentAccountEntryRetry(): Response<PaymentEntryResponse>

}

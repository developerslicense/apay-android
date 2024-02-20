package kz.airbapay.apay_android.network.api

import kz.airbapay.apay_android.data.model.AuthRequest
import kz.airbapay.apay_android.data.model.AuthResponse
import kz.airbapay.apay_android.data.model.CardsGetResponse
import kz.airbapay.apay_android.data.model.CardsPanResponse
import kz.airbapay.apay_android.data.model.GetCvvResponse
import kz.airbapay.apay_android.data.model.GooglePayButtonResponse
import kz.airbapay.apay_android.data.model.GooglePayMerchantResponse
import kz.airbapay.apay_android.data.model.GooglePaymentWalletRequest
import kz.airbapay.apay_android.data.model.MerchantsResponse
import kz.airbapay.apay_android.data.model.PaymentCreateResponse
import kz.airbapay.apay_android.data.model.PaymentEntryResponse
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

    @GET("api/v1/cards/{accountId}")
    suspend fun getCards(@Path("accountId") accountId: String): Response<CardsGetResponse>

    @GET("api/v1/cards/info-by-pan/{pan}")
    suspend fun getCardsBank(@Path("pan") pan: String): Response<CardsPanResponse>

    @DELETE("api/v1/cards/{id}")
    suspend fun deleteCard(@Path("id") cardId: String): Response<Any>

    // google pay

    @GET("api/v1/wallets/google-pay/button")
    suspend fun getGooglePayButton(): Response<GooglePayButtonResponse>

    @GET("api/v1/wallets/google-pay/merchant")
    suspend fun getGooglePayMerchant(): Response<GooglePayMerchantResponse>

    // payments

    @POST("api/v1/payments")
    suspend fun createPayment(
        @Body param: HashMap<String, Any?>
    ): Response<PaymentCreateResponse>

    @PUT("api/v1/payments")
    suspend fun putPayment(
        @Body param: HashMap<String, Any?>
    ): Response<PaymentEntryResponse>

    @PUT("api/v1/payments/wallet")
    suspend fun putPaymentWallet(
        @Body param: GooglePaymentWalletRequest
    ): Response<PaymentEntryResponse>

    @PUT("api/v1/payments/{cardId}")
    suspend fun putPayment(
        @Path("cardId") cardId: String,
        @Body param: HashMap<String, Any?>
    ): Response<PaymentEntryResponse>

    @PUT("api/v1/payments/retry") // проводка без ввода данных карты
    suspend fun paymentAccountEntryRetry(): Response<PaymentEntryResponse>

    @GET("api/v1/payments/cvv/{cardId}")
    suspend fun getCvv(
        @Path("cardId") cardId: String
    ): Response<GetCvvResponse>

    @GET("api/v1/merchants")
    suspend fun getMerchantInfo(): Response<MerchantsResponse>
}

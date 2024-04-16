/*
 * Copyright 2024 Google Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import android.content.Context
import com.google.android.gms.wallet.PaymentsClient
import com.google.android.gms.wallet.Wallet
import com.google.android.gms.wallet.WalletConstants
import kz.airbapay.apay_android.data.utils.DataHolder
import org.json.JSONArray
import org.json.JSONException
import org.json.JSONObject

object GooglePayUtil {

    private val baseRequest = JSONObject()
        .put("apiVersion", 2)
        .put("apiVersionMinor", 0)

    private fun gatewayTokenizationSpecification(): JSONObject =
        JSONObject()
            .put("type", "PAYMENT_GATEWAY")
            .put(
                "parameters",
                JSONObject(mapOf(
                    "gateway" to DataHolder.gateway,
                    "gatewayMerchantId" to DataHolder.gatewayMerchantId
                )
            ))

    private val allowedCardNetworks = JSONArray(listOf(
        "AMEX",
        "MASTERCARD",
        "VISA")
    )

    private val allowedCardAuthMethods = JSONArray(
        listOf(
            "PAN_ONLY",
            "CRYPTOGRAM_3DS"
        )
    )

    private fun baseCardPaymentMethod(): JSONObject =
        JSONObject()
            .put("type", "CARD")
            .put(
                "parameters", JSONObject()
                    .put("allowedAuthMethods", allowedCardAuthMethods)
                    .put("allowedCardNetworks", allowedCardNetworks)
                    .put("billingAddressRequired", true)
                    .put(
                        "billingAddressParameters", JSONObject()
                            .put("format", "FULL")
                    )
            )

    private fun cardPaymentMethod(): JSONObject = baseCardPaymentMethod()
        .put("tokenizationSpecification", gatewayTokenizationSpecification())

    fun allowedPaymentMethods(): JSONArray = JSONArray().put(cardPaymentMethod())

    fun isReadyToPayRequest(): JSONObject? =
        try {
            baseRequest
                .put("allowedPaymentMethods", JSONArray().put(baseCardPaymentMethod()))
        } catch (e: JSONException) {
            null
        }

    private val merchantInfo: JSONObject =
        JSONObject().put("merchantName", "AirbaPay")

    fun createPaymentsClient(context: Context): PaymentsClient {
        val walletOptions = Wallet.WalletOptions.Builder()
            .setEnvironment(if(DataHolder.isProd) WalletConstants.ENVIRONMENT_PRODUCTION else WalletConstants.ENVIRONMENT_TEST)
            .build()

        return Wallet.getPaymentsClient(context, walletOptions)
    }

    private fun getTransactionInfo(price: String): JSONObject =
        JSONObject()
            .put("totalPrice", price)
            .put("totalPriceStatus", "FINAL")
            .put("countryCode", "KZ")
            .put("currencyCode", "KZT")

    fun getPaymentDataRequest(price: String): JSONObject =
        baseRequest
            .put("allowedPaymentMethods", allowedPaymentMethods())
            .put("transactionInfo", getTransactionInfo(price))
            .put("merchantInfo", merchantInfo)
            .put("shippingAddressRequired", true)
            .put(
                "shippingAddressParameters", JSONObject()
                    .put("phoneNumberRequired", false)
                    .put("allowedCountryCodes", JSONArray(listOf("KZ")))
            )
}

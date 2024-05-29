package kz.airbapay.apay_android.ui.pages.startview

import android.app.Activity
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openHome
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.bl_components.saved_cards.blGetSavedCards

internal fun fetchMerchantsWithNextStep(
    activity: Activity,
    googlePayRedirectUrl: MutableState<String?>,
    savedCards: MutableState<List<BankCard>>,
    selectedCard: MutableState<BankCard?>,
    isLoading: MutableState<Boolean>

) {
    Repository.googlePayRepository?.getGooglePayMerchant(
        result = {
            DataHolder.gatewayMerchantId = it.gatewayMerchantId
            DataHolder.gateway = it.gateway
        },
        error = {}
    )

    Repository.paymentsRepository?.getPaymentInfo(
        result = {
            initPaymentsWithNextStep(
                activity = activity,
                googlePayRedirectUrl = googlePayRedirectUrl,
                savedCards = savedCards,
                selectedCard = selectedCard,
                isLoading = isLoading
            )
        },
        error = {
            openErrorPageWithCondition(
                errorCode = ErrorsCode.error_1.code,
                activity = activity
            )
        }
    )
}

private fun initPaymentsWithNextStep(
    activity: Activity,
    googlePayRedirectUrl: MutableState<String?>,
    savedCards: MutableState<List<BankCard>>,
    selectedCard: MutableState<BankCard?>,
    isLoading: MutableState<Boolean>
) {
    val onGooglePayResult = { url: String? ->
        if (url != null) {
            DataHolder.googlePayButtonUrl = url
            googlePayRedirectUrl.value = url
        }

        if (DataHolder.isRenderSavedCards()) {
            blGetSavedCards(
                onSuccess = {
                    savedCards.value = it
                    DataHolder.hasSavedCards = it.isNotEmpty()

                    if (it.isEmpty()) {
                        openHome(activity)
                    } else {
                        selectedCard.value = it[0]
                        isLoading.value = false
                    }
                },
                onNoCards = {
                    openHome(activity)
                }
            )

        } else {
            openHome(activity)
        }
    }

    if (DataHolder.isGooglePayNative) {
        onGooglePayResult(null)

    } else {
        Repository.googlePayRepository?.getGooglePayButton(
            result = { response ->
                onGooglePayResult(response.buttonUrl)
            },
            error = {
                onGooglePayResult(null)
            }
        )
    }
}


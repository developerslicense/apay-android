package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openHome
import kz.airbapay.apay_android.network.repository.Repository

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

    initPaymentsWithNextStep(
        activity = activity,
        googlePayRedirectUrl = googlePayRedirectUrl,
        savedCards = savedCards,
        selectedCard = selectedCard,
        isLoading = isLoading
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

        if (DataHolder.renderInStandardFlowSavedCards) {
            fetchCards(
                activity = activity,
                savedCards = savedCards,
                selectedCard = selectedCard,
                isLoading = isLoading
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

private fun fetchCards(
    activity: Activity,
    savedCards: MutableState<List<BankCard>>,
    selectedCard: MutableState<BankCard?>,
    isLoading: MutableState<Boolean>
) {
    Repository.cardRepository?.getCards(
        accountId = DataHolder.accountId,
        error = {
            openHome(activity)
        },
        result = {
            savedCards.value = it
            DataHolder.hasSavedCards = it.isNotEmpty()

            if (it.isEmpty()) {
                openHome(activity)
            } else {
                selectedCard.value = it[0]
                isLoading.value = false
            }
        }
    )
}
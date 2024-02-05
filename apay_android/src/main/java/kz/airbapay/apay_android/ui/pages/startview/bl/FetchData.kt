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
    Repository.merchantRepository?.getMerchantInfo(
        result = { response ->
            DataHolder.featureSavedCards = response.configuration?.renderSaveCards ?: false
            DataHolder.featureGooglePay = response.configuration?.renderGooglePayButton ?: false

            initPaymentsWithNextStep(
                activity = activity,
                googlePayRedirectUrl = googlePayRedirectUrl,
                savedCards = savedCards,
                selectedCard = selectedCard,
                isLoading = isLoading
            )
        },
        error = {
            initPaymentsWithNextStep(
                activity = activity,
                googlePayRedirectUrl = googlePayRedirectUrl,
                savedCards = savedCards,
                selectedCard = selectedCard,
                isLoading = isLoading
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
    initPayments(
        activity = activity,
        onGooglePayResult = { url ->
            if (url != null) {
                DataHolder.googlePayButtonUrl = url
                googlePayRedirectUrl.value = url
            }

            if (DataHolder.featureSavedCards) {
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
    )
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

            if (it.isEmpty()) {
                openHome(activity)
            } else {
                selectedCard.value = it[0]
                isLoading.value = false
            }
        }
    )
}
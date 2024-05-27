package kz.airbapay.apay_android.ui.bl_components.saved_cards

import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.network.repository.Repository

internal fun blGetSavedCards(
    onSuccess: (List<BankCard>) -> Unit,
    onNoCards: () -> Unit
) {
    Repository.cardRepository?.getCards(
        accountId = DataHolder.accountId,
        error = { onNoCards() },
        result = { onSuccess(it) }
    )
}
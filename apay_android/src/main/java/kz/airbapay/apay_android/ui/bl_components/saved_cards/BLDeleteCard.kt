package kz.airbapay.apay_android.ui.bl_components.saved_cards

import kz.airbapay.apay_android.network.repository.Repository

internal fun blDeleteCard(
    cardId: String,
    onSuccess: () -> Unit,
    onError: () -> Unit
) {
    Repository.cardRepository?.deleteCard(
        cardId = cardId,
        result = { onSuccess() },
        error = { onError() }
    )
}
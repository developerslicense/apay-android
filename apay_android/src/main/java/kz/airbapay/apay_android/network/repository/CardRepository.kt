package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.model.CardAddRequest
import kz.airbapay.apay_android.data.model.CardAddResponse
import kz.airbapay.apay_android.data.utils.card_utils.CardType
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class CardRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

    fun getCards(
        phone: String,
        result: (List<BankCard>) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.getCards(phone)
                }
            },
            result = { body ->
                body.body()?.let {
                    result(
                        it.map { card ->
                            card.copy(
                                typeIcon = when (card.type) {
                                    "VISA" -> CardType.VISA.icon
                                    "MC" -> CardType.MASTER_CARD.icon
                                    "AE" -> CardType.AMERICAN_EXPRESS.icon
                                    else -> CardType.MAESTRO.icon
                                }
                            )
                        }
                    )
                } ?: result(emptyList())
            },
            error = error
        )
    }

    fun cardAdd(
        param: CardAddRequest,
        result: (CardAddResponse) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.cardAdd(param)
                }
            },
            result = { body ->
                body.body()?.let {
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }

    fun deleteCard(
        cardId: String,
        result: (Any) -> Unit,
        error: (Response<*>) -> Unit
    ) {
        launch(
            requestFlow = {
                safeApiFlowCall {
                    api.deleteCard(cardId)
                }
            },
            result = { body ->
                body.body()?.let {
                    result(it)
                } ?: error(Unit)
            },
            error = error
        )
    }
}
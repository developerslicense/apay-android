package kz.airbapay.apay_android.network.repository

import kz.airbapay.apay_android.data.model.CardAddRequest
import kz.airbapay.apay_android.data.model.CardAddResponse
import kz.airbapay.apay_android.data.model.CardsGetResponse
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.safeApiFlowCall
import kz.airbapay.apay_android.network.coroutines.BaseCoroutine
import kz.airbapay.apay_android.network.coroutines.BaseCoroutineDelegate
import retrofit2.Response

internal class CardRepository(
    private val api: Api
) : BaseCoroutine by BaseCoroutineDelegate() {

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

    fun getCards(
        phone: String,
        result: (CardsGetResponse) -> Unit,
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
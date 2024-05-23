package kz.airbapay.apay_android.ui.pages.test_page

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.material.Button
import androidx.compose.material.Text
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView

internal class TestCardsExternalActivity : ComponentActivity() {

    private val isLoading = mutableStateOf(true)
    var coroutineScope: CoroutineScope? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        testInitSdk(
            context = this@TestCardsExternalActivity,
            autoCharge = 0
        )

        setContent {
            coroutineScope = rememberCoroutineScope()

            Column(

                modifier = Modifier
            ) {
                Button(
                    onClick = { this@TestCardsExternalActivity.finish() },
                    content = {
                        Text("Нет карт. Вернуться назад")
                    }
                )
            }



            if (isLoading.value) {
                ProgressBarView()
            }
        }
    }
}

/*
airbaPaySdk.auth(
                            onSuccess: {
                                airbaPaySdk.getCards(
                                        onSuccess: { cards in
                                            savedCards = []
                                            cards.forEach { card in
                                                if card.getMaskedPanClearedWithPoint().contains("1111") {

                                                    var card1 = card
                                                    card1.name = card.getMaskedPanClearedWithPoint() + " Оплата сохраненной картой c FaceId"
                                                    savedCards.append(card1)

                                                    var card2 = card
                                                    card2.name = card.getMaskedPanClearedWithPoint() + " Оплата сохраненной картой без FaceId"
                                                    savedCards.append(card2)

                                                } else {
                                                    var card1 = card
                                                    card1.name = card.getMaskedPanClearedWithPoint() + " Оплата сохраненной картой CVV"
                                                    savedCards.append(card1)
                                                }
                                            }
                                        },
                                        onNoCards: { }
                                )
                            },
                            onError: {

                            }
                    )
 */

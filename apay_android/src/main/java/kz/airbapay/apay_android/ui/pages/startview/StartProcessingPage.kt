package kz.airbapay.apay_android.ui.pages.startview

import android.app.Activity
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.R
import kz.airbapay.apay_android.data.constant.paymentByCard
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.AirbaPayBiometric
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.recomposeHighlighter
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.network.repository.startAuth
import kz.airbapay.apay_android.ui.pages.googlepay.GooglePayPage
import kz.airbapay.apay_android.ui.pages.startview.bl.initPayments
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitErrorState
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingAmount
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingButtonNext
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.InitViewStartProcessingCards
import kz.airbapay.apay_android.ui.resources.ColorsSdk
import kz.airbapay.apay_android.ui.ui_components.BackHandler
import kz.airbapay.apay_android.ui.ui_components.ProgressBarView
import kz.airbapay.apay_android.ui.ui_components.ViewToolbar

internal class StartProcessingActivity: ComponentActivity() {
    private val isAuthenticated = mutableStateOf(false)

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val airbaPayBiometric = AirbaPayBiometric(this)
        airbaPayBiometric.authenticate(
            onSuccess = {
                isAuthenticated.value = true
            },
            onError = {
                isAuthenticated.value = false
            }
        )

        setContent {
            StartProcessingPage(
                isAuthenticated = isAuthenticated,
                actionClose = { this@StartProcessingActivity.finish() }
            )
        }
    }
}

@Composable
internal fun StartProcessingPage(
    actionClose: () -> Unit,
    backgroundColor: Color = ColorsSdk.bgBlock,
    isAuthenticated: MutableState<Boolean>
) {
    val activity = LocalContext.current as Activity
    val googlePayRedirectUrl = remember {
        mutableStateOf<String?>(null)
    }

    BackHandler {
        actionClose()
    }

    val purchaseAmount = DataHolder.purchaseAmountFormatted.collectAsState()

    val isError = remember { mutableStateOf(false) }
    val size = remember { mutableStateOf(IntSize.Zero) }
    val isLoading = remember { mutableStateOf(true) }
    val selectedCard = remember { mutableStateOf<BankCard?>(null) }

    val savedCards = remember {
        mutableStateOf<List<BankCard>>(emptyList())
    }

    Card(
        backgroundColor = backgroundColor,
        shape = RoundedCornerShape(
            topStart = 0.dp,
            topEnd = 0.dp
        ),
        modifier = Modifier
            .recomposeHighlighter()
            .fillMaxSize()
            .onSizeChanged {
                size.value = it
            },
        elevation = 0.dp
    ) {
        if (!isLoading.value) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.recomposeHighlighter()
            ) {

                ViewToolbar(
                    title = paymentByCard(),
                    backIcon = R.drawable.ic_arrow_back,
                    actionBack = actionClose
                )

                if (isError.value) {
                    InitErrorState()

                } else {
                    InitViewStartProcessingAmount(purchaseAmount.value)

                      if (googlePayRedirectUrl.value != null) {
                          GooglePayPage(
                              url = googlePayRedirectUrl.value
                          )
                      }

                    if (savedCards.value.isNotEmpty()
                        && isAuthenticated.value
                    ) {
                        InitViewStartProcessingCards(
                            savedCards = savedCards.value,
                            selectedCard = selectedCard,
                            actionClose = actionClose
                        )
                    }

                    InitViewStartProcessingButtonNext(
                        isLoading = isLoading,
                        savedCards = savedCards.value,
                        purchaseAmount = purchaseAmount.value,
                        isAuthenticated = isAuthenticated.value,
                        selectedCard = selectedCard
                    )
                }
            }
        }

        if (isLoading.value) {
            ProgressBarView(
                size = size,
                modifier = Modifier.wrapContentHeight()
            )
        }
    }

    LaunchedEffect("CardRepository") {
        launch {
            isError.value = false

            startAuth(
                authRepository = Repository.authRepository!!,
                onError = {
                    isError.value = true
                    isLoading.value = false
                },
                onResult = {

                    Repository.cardRepository?.getCards(
                        accountId = DataHolder.accountId,
                        error = {
                            isLoading.value = false
                        },
                        result = {
                            isLoading.value = false
                            savedCards.value = it

                            if (it.isNotEmpty()) {
                                selectedCard.value = it[0]
                            }
                        }
                    )

                    initPayments(
                        activity = activity,
                        isLoading = isLoading,
                        onGooglePayLoadSuccess = { url ->
                            googlePayRedirectUrl.value = url
                        }
                    )
                }
            )
        }
    }
}



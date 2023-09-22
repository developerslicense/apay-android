package kz.airbapay.apay_android

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.text.TextRange
import androidx.compose.ui.text.input.TextFieldValue
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import androidx.navigation.navDeepLink
import kz.airbapay.apay_android.data.constant.ARG_ACTION
import kz.airbapay.apay_android.data.constant.ARG_CARD_ID
import kz.airbapay.apay_android.data.constant.ARG_ERROR_CODE
import kz.airbapay.apay_android.data.constant.ROUTES_ERROR
import kz.airbapay.apay_android.data.constant.ROUTES_ERROR_FINAL
import kz.airbapay.apay_android.data.constant.ROUTES_ERROR_SOMETHING_WRONG
import kz.airbapay.apay_android.data.constant.ROUTES_ERROR_WITH_INSTRUCTION
import kz.airbapay.apay_android.data.constant.ROUTES_HOME
import kz.airbapay.apay_android.data.constant.ROUTES_REPEAT
import kz.airbapay.apay_android.data.constant.ROUTES_SUCCESS
import kz.airbapay.apay_android.data.constant.ROUTES_WEB_VIEW
import kz.airbapay.apay_android.data.constant.initErrorsCodeByCode
import kz.airbapay.apay_android.data.utils.MaskUtils
import kz.airbapay.apay_android.network.api.Api
import kz.airbapay.apay_android.network.base.ClientConnector
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.CardRepository
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.ui.pages.card_reader.ScanActivity
import kz.airbapay.apay_android.ui.pages.error.ErrorFinalPage
import kz.airbapay.apay_android.ui.pages.error.ErrorPage
import kz.airbapay.apay_android.ui.pages.error.ErrorSomethingWrongPage
import kz.airbapay.apay_android.ui.pages.error.ErrorWithInstructionPage
import kz.airbapay.apay_android.ui.pages.error.RepeatPage
import kz.airbapay.apay_android.ui.pages.home.HomePage
import kz.airbapay.apay_android.ui.pages.success.SuccessPage
import kz.airbapay.apay_android.ui.pages.webview.WebViewPage
import java.lang.ref.WeakReference

class AirbaPayActivity : ComponentActivity() {

    var scanResultLauncher: ActivityResultLauncher<Intent>? = null
    private val cardNumberText = mutableStateOf(TextFieldValue())

    companion object {
        private var customSuccessPage: WeakReference<@Composable () -> Unit>? = null

        fun init(
            context: Context,
            cardId: String? = null,
            customSuccessPage: @Composable (() -> Unit)?
        ) {
            val intent = Intent(context, AirbaPayActivity::class.java)
            if (cardId != null) {
                intent.putExtra(ARG_CARD_ID, cardId)
            }
            this.customSuccessPage = WeakReference(customSuccessPage)
            context.startActivity(intent)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val clientConnector = ClientConnector(this)
        val api = clientConnector.retrofit.create(Api::class.java)

        val authRepository = AuthRepository(api)
        val paymentsRepository = PaymentsRepository(api)
        val cardRepository = CardRepository(api)

        val selectedCardId: String? = intent.getStringExtra(ARG_CARD_ID)

        val localSuccessCustomPage: @Composable (() -> Unit)? = customSuccessPage?.get()
        customSuccessPage = null

        scanResultLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    val data = result.data
                    val cardNumber = data?.extras?.getString(ScanActivity.RESULT_CARD_NUMBER)
                    val maskUtils = MaskUtils("AAAA AAAA AAAA AAAA")

                    cardNumberText.value = TextFieldValue(
                        text = maskUtils.format(cardNumber ?: ""),
                        selection = TextRange(cardNumber?.length ?: 0)
                    )
                }
            }

        setContent {
            val navController = rememberNavController()

            NavHost(
                navController = navController,
                startDestination = ROUTES_HOME
            ) {
                composable(ROUTES_HOME) {
                    HomePage(
                        cardNumberText = cardNumberText,
                        navController = navController,
                        authRepository = authRepository,
                        paymentsRepository = paymentsRepository,
                        cardRepository = cardRepository,
                        selectedCardId = selectedCardId
                    )
                }

                composable(
                    route = ROUTES_ERROR,
                    deepLinks = listOf(
                        navDeepLink { uriPattern = ROUTES_ERROR }
                    ),
                    arguments = listOf(
                        navArgument(ARG_ERROR_CODE) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    ErrorPage(
                        errorCode = initErrorsCodeByCode(
                            (backStackEntry.arguments?.getString(ARG_ERROR_CODE) ?: "1").toInt()
                        ),
                        navController = navController
                    )
                }

                composable(ROUTES_ERROR_FINAL) {
                    ErrorFinalPage()
                }

                composable(ROUTES_ERROR_SOMETHING_WRONG) {
                    ErrorSomethingWrongPage()
                }

                composable(
                    route = ROUTES_ERROR_WITH_INSTRUCTION,
                    deepLinks = listOf(
                        navDeepLink { uriPattern = ROUTES_ERROR_WITH_INSTRUCTION }
                    ),
                    arguments = listOf(
                        navArgument(ARG_ERROR_CODE) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    ErrorWithInstructionPage(
                        errorCode = initErrorsCodeByCode(
                            (backStackEntry.arguments?.getString(ARG_ERROR_CODE) ?: "1").toInt()
                        ),
                        navController = navController
                    )
                }

                composable(ROUTES_REPEAT) {
                    RepeatPage(
                        navController = navController,
                        paymentsRepository = paymentsRepository
                    )
                }

                composable(
                    route = ROUTES_WEB_VIEW,
                    deepLinks = listOf(
                        navDeepLink { uriPattern = ROUTES_WEB_VIEW }
                    ),
                    arguments = listOf(
                        navArgument(ARG_ACTION) {
                            type = NavType.StringType
                        }
                    )
                ) { backStackEntry ->
                    WebViewPage(
                        url = backStackEntry.arguments?.getString(ARG_ACTION).orEmpty(),
                        navController = navController
                    )
                }

                composable(ROUTES_SUCCESS) {
                    SuccessPage(customSuccessPage = localSuccessCustomPage)
                }
            }

        }
    }
}


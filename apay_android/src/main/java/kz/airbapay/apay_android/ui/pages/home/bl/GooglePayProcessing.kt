package kz.airbapay.apay_android.ui.pages.home.bl

import androidx.compose.runtime.MutableState
import androidx.navigation.NavController
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openGooglePay
import kz.airbapay.apay_android.network.repository.AuthRepository
import kz.airbapay.apay_android.network.repository.GooglePayRepository
import kz.airbapay.apay_android.network.repository.PaymentsRepository
import kz.airbapay.apay_android.network.repository.startAuth

internal fun startPaymentProcessingGooglePay(
    navController: NavController,
    isLoading: MutableState<Boolean>,
    authRepository: AuthRepository,
    paymentsRepository: PaymentsRepository,
    googlePayRepository: GooglePayRepository,
    coroutineScope: CoroutineScope
) {
    isLoading.value = true

    coroutineScope.launch {

        startCreatePayment(
            paymentsRepository = paymentsRepository,
            authRepository = authRepository,
            googlePayRepository = googlePayRepository,
            onError = { errorCode ->
                openErrorPageWithCondition(
                    errorCode = errorCode.code,
                    navController = navController
                )
            },
            onSuccess = { redirectUrl ->
                openGooglePay(
                    navController = navController,
                    redirectUrl = redirectUrl
                )
            }
        )
    }
}

private fun startCreatePayment(
    onSuccess: (String) -> Unit,
    onError: (ErrorsCode) -> Unit,
    authRepository: AuthRepository,
    paymentsRepository: PaymentsRepository,
    googlePayRepository: GooglePayRepository
) {
    paymentsRepository.createPayment(
        result = {

            startAuth(
                authRepository = authRepository,
                paymentId = it.id,
                onError = {
                    onError(ErrorsCode.error_1)
                },
                onResult = {
                    googlePayRepository.getGooglePay(
                        result = { response ->
                            onSuccess(response.buttonUrl ?: "")
                        },
                        error = {
                            onError(ErrorsCode.error_1)
                        }
                    )
                }
            )
        },
        error = {
            onError(ErrorsCode.error_1)
        }
    )
}





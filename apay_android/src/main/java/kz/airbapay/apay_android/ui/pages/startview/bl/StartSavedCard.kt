package kz.airbapay.apay_android.ui.pages.startview.bl

import android.app.Activity
import androidx.compose.runtime.MutableState
import kotlinx.coroutines.CoroutineScope
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openAcquiring
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.data.utils.openSuccess
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.ui_components.initAuth

internal fun checkNeedCvv(
    activity: Activity,
    cardId: String,
    showCvv: () -> Unit,
    isLoading: MutableState<Boolean>? = null,
    coroutineScope: CoroutineScope
) {

    Repository.paymentsRepository?.paymentGetCvv(
        cardId = cardId,
        result = {

            if (it.requestCvv
                || !DataHolder.renderGlobalSecurityBiometry
                && DataHolder.renderGlobalSecurityCvv
            ) {
                showCvv()

            } else if(DataHolder.renderGlobalSecurityBiometry) {
                initAuth(
                    activity = activity,
                    coroutineScope = coroutineScope,
                    onSuccess = {
                        isLoading?.value = true
                        startSavedCard(
                            cardId = cardId,
                            cvv = null,
                            isLoading = isLoading,
                            activity = activity
                        )
                    },
                    onNotSecurity = {
                        if(DataHolder.renderGlobalSecurityCvv) {
                            showCvv()

                        } else {
                            isLoading?.value = true
                            startSavedCard(
                                cardId = cardId,
                                cvv = null,
                                isLoading = isLoading,
                                activity = activity
                            )
                        }
                    }
                )

            } else {
                isLoading?.value = true
                startSavedCard(
                    cardId = cardId,
                    cvv = null,
                    isLoading = isLoading,
                    activity = activity
                )
            }
        },
        error = {
            onError(
                isLoading = isLoading,
                activity = activity
            )
        }
    )
}

internal fun startSavedCard(
    cardId: String,
    cvv: String?,
    isLoading: MutableState<Boolean>?,
    activity: Activity
) {
    Repository.paymentsRepository?.startPaymentSavedCard(
        cardId = cardId,
        cvv = cvv,
        result = {
            when (it.status) {

                "success",
                "auth" -> {
                    openSuccess(activity)
                }

                "secure3D" -> {
                    openAcquiring(
                        redirectUrl = it.secure3D?.action,
                        activity = activity
                    )
                }

                "error" -> {
                    isLoading?.value = false
                    openErrorPageWithCondition(
                        errorCode = ErrorsCode.error_5006.code,
                        activity = activity
                    )
                }
            }
        },
        error = {
            onError(
                isLoading = isLoading,
                activity = activity
            )
        }
    )
}

private fun onError(
    isLoading: MutableState<Boolean>?,
    activity: Activity
) {
    isLoading?.value = false
    openErrorPageWithCondition(
        errorCode = ErrorsCode.error_5006.code,
        activity = activity
    )
}

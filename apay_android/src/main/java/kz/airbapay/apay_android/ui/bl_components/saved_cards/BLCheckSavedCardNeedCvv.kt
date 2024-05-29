package kz.airbapay.apay_android.ui.bl_components.saved_cards

import android.app.Activity
import android.widget.Toast
import androidx.compose.runtime.Composable
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.platform.LocalFocusManager
import kz.airbapay.apay_android.data.constant.ErrorsCode
import kz.airbapay.apay_android.data.constant.cvvInfo
import kz.airbapay.apay_android.data.model.BankCard
import kz.airbapay.apay_android.data.utils.DataHolder
import kz.airbapay.apay_android.data.utils.openErrorPageWithCondition
import kz.airbapay.apay_android.network.repository.Repository
import kz.airbapay.apay_android.ui.pages.startview.start_processing_ext.EnterCvvBottomSheet
import kz.airbapay.apay_android.ui.ui_components.initAuth
import kz.airbapay.apay_android.ui.ui_components.showAsBottomSheet

internal fun blCheckSavedCardNeedCvv(
    activity: Activity,
    selectedCard: BankCard,
    isLoading: (Boolean) -> Unit,
    onError: (() -> Unit)?,
    showCvv: () -> Unit = {
        val content: @Composable (() -> Unit) -> Unit = { hideBottomSheet ->
            val focusManager = LocalFocusManager.current
            val cvvFocusRequester = FocusRequester()

            EnterCvvBottomSheet(
                actionClose = {
                    focusManager.clearFocus()
                    hideBottomSheet()
                },
                cardMasked = selectedCard.getMaskedPanClearedWithPoint(),
                isLoading = isLoading,
                cardId = selectedCard.id,
                cvvFocusRequester = cvvFocusRequester,
                showCvvInfo = {
                    Toast.makeText(activity, cvvInfo(), Toast.LENGTH_SHORT).show()
                }
            )
        }

        activity.showAsBottomSheet(content)
    }
) {

    Repository.paymentsRepository?.paymentGetCvv(
        cardId = selectedCard.id!!,
        result = {

            if (it.requestCvv) {
                showCvv()

            } else if (DataHolder.isRenderSecurityBiometry()) {
                initAuth(
                    activity = activity,
                    onSuccess = {
                        isLoading(true)
                        blProcessSavedCard(
                            cardId = selectedCard.id,
                            cvv = null,
                            isLoading = isLoading,
                            activity = activity
                        )
                    },
                    onNotSecurity = {
                        if (DataHolder.isRenderSecurityCvv()) {
                            showCvv()

                        } else {
                            isLoading(true)
                            blProcessSavedCard(
                                cardId = selectedCard.id,
                                cvv = null,
                                isLoading = isLoading,
                                activity = activity
                            )
                        }
                    }
                )

            } else if (DataHolder.isRenderSecurityCvv()) {// не объединять с 1-м, т.к. у этого приоритет ниже, чем у renderSecurityBiometry
                showCvv()

            } else {
                isLoading(true)
                blProcessSavedCard(
                    cardId = selectedCard.id,
                    cvv = null,
                    isLoading = isLoading,
                    activity = activity
                )
            }
        },
        error = {
            isLoading(false)

            if (onError != null) {
                onError()
            } else {
                openErrorPageWithCondition(
                    errorCode = ErrorsCode.error_5006.code,
                    activity = activity
                )
            }
        }
    )
}



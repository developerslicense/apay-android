package kz.airbapay.apay_android.ui.ui_components

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.AirbaPayBiometric
import kz.airbapay.apay_android.data.utils.DataHolder

internal fun initAuth(
    activity: Activity,
    coroutineScope: CoroutineScope,
    onSuccess: () -> Unit,
    onNotSecurity: () -> Unit,
) {
    if (DataHolder.renderGlobalSecurityBiometry) {
        coroutineScope.launch {
            val airbaPayBiometric = AirbaPayBiometric(activity)
            airbaPayBiometric.authenticate(
                onSuccess = onSuccess,
                onError = {},
                onNotSecurity = onNotSecurity
            )
        }
    } else {
        onNotSecurity()
    }
}
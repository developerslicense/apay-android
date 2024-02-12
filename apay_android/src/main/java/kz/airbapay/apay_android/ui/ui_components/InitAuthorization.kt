package kz.airbapay.apay_android.ui.ui_components

import android.app.Activity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import kz.airbapay.apay_android.data.utils.AirbaPayBiometric

internal fun initAuth(
    activity: Activity,
    coroutineScope: CoroutineScope,
    onSuccess: () -> Unit,
    onFailed: () -> Unit,
    onNotSecurity: () -> Unit,
) {
    coroutineScope.launch {
        val airbaPayBiometric = AirbaPayBiometric(activity)
        airbaPayBiometric.authenticate(
            onSuccess = onSuccess,
            onError = onFailed,
            onNotSecurity = onNotSecurity
        )
    }
}
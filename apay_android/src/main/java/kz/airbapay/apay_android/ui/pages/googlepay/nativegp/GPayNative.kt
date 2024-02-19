package kz.airbapay.apay_android.ui.pages.googlepay.nativegp

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import com.google.android.gms.wallet.button.ButtonConstants
import com.google.android.gms.wallet.button.ButtonOptions
import com.google.android.gms.wallet.button.PayButton
import kz.airbapay.apay_android.data.utils.DataHolder

@Composable
internal fun GPayNative(
    onClick: () -> Unit,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp)
) {

    val allowedPaymentMethods = """
            [
              {
                "type": "CARD",
                "parameters": {
                  "allowedAuthMethods": ["PAN_ONLY", "CRYPTOGRAM_3DS"],
                  "allowedCardNetworks": ["AMEX", "MASTERCARD", "VISA"]
                },
                "tokenizationSpecification": {
                  "type": "PAYMENT_GATEWAY",
                  "parameters": {
                    "gateway": "${DataHolder.gateway}",
                    "gatewayMerchantId": "${DataHolder.gatewayMerchantId}"
                  }
                }
              }
            ]
        """.trimIndent()

    // allowedCardNetworks "DISCOVER", "JCB",

    AndroidView(
        modifier = modifier,
        factory = { context ->
            PayButton(context).apply {
                this.initialize(
                    ButtonOptions.newBuilder()
                        .setButtonTheme(ButtonConstants.ButtonTheme.DARK)
                        .setButtonType(ButtonConstants.ButtonType.PLAIN)
                        .setCornerRadius(8)
                        .setAllowedPaymentMethods(allowedPaymentMethods)
                        .build()
                )
            }
        },
        update = { button ->
            println("click native aaaaaaaaaaaaaaa")
            println(DataHolder.gateway)
            println(DataHolder.gatewayMerchantId)

            button.apply {
                setOnClickListener { onClick() }
            }
        }
    )
}
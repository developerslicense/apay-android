package kz.airbapay.apay_android.data.model

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import kz.airbapay.apay_android.ui.resources.ColorsSdk

data class EditTextDVO(
    val needIcon: Boolean = false,
    val endIcon: Int? = null,
    val placeholder: String? = null,
    val errorTitle: String? = null,
    val maxLines: Int = 1,
    val editTextFieldBackgroundDefault: Color = ColorsSdk.bgElements,
    val needShowError: Boolean = true,
    val stateEnabled: Boolean = true,
    val stateError: Boolean = false,
    val stateFocused: Boolean = false,
    val stateLoading: Boolean = false,
    val stateSearchIconEnd: Boolean = false,
    val isMoney: Boolean = false,
    val id: String? = null,
    val regexForClear: Regex? = null,
    val regexForCheck: Regex? = null,
    val mask: String? = null,

    val textLengthLimit: Int? = null,
    val actionTextChanged: ((text: String) -> Unit)? = null,
    val actionCheckValid: ((isValid: Boolean) -> Unit)? = null,
    val actionClickIcon: (() -> Unit)? = null,
    val keyboardActions: KeyboardActions,
    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.None,
        autoCorrect = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Next
    )
) {

    fun getPlaceholderText() = placeholder
}

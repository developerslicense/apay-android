package kz.airbapay.apay_android.data.model

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
    val title: String? = null,
    val errorTitle: String? = null,
    val maxLines: Int = 1,
    val titleColor: Color = ColorsSdk.buttonMainBrandMS.value,
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
    val actionClickFieldForGoToSearch: (() -> Unit)? = null,
    val keyboardActions: (() -> Unit)? = null,

    val keyboardOptions: KeyboardOptions = KeyboardOptions.Default.copy(
        capitalization = KeyboardCapitalization.Sentences,
        autoCorrect = false,
        keyboardType = KeyboardType.Text,
        imeAction = ImeAction.Done
    )
) {
    val stateForSearch: Boolean = actionClickFieldForGoToSearch != null

    fun getTitleText() = title
    fun getPlaceholderText() = placeholder
}

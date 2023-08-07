package kz.airbapay.apay_android.ui.resources

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

internal object ColorsSdk {// todo надо предоставить внешнему пользователю возможность изменить некоторые цвета
    /** добавил MS к некоторым на конец для того,
     * чтоб понятно было, какие цвета редактируются для внешнего пользователя*/

    val colorBrandMainMS = mutableStateOf(Color(0xFF319CF3)) //можно будет изменять
    val colorBrandInversionMS = mutableStateOf(Color(0xFFE6E9FA)) //можно будет изменять

    // block
    val bgBlock = Color(0xFFFFFFFF)
    val bgMain = Color(0xF3F4FBFF)
    val bgAccent = Color(0xFF10142D)
    val bgSecondaryAccent = Color(0xFFFFF3C8)
    val bgElements = Color(0xFFFCFCFF)

    // text
    val textMain = Color(0xFF10142D)
    val textLight = Color(0xFF787E9E)
    val textSecondary = Color(0xFF383E61)
    val textInversion = Color(0xFFFCFCFD)

    // icons
    val iconMain = Color(0xFF10142D)
    val iconSecondary = Color(0xFF787E9E)
    val iconInversion = Color(0xFFFCFCFD)

    // buttons
    val buttonSecondaryDelete = Color(0xFFFFFBEE)
    val buttonDefault = Color(0xFFFFFFFF)

    // state
    val stateSuccess = Color(0xFF1ACE37)
    val stateBdSuccess = Color(0xFFC7F9EF)
    val stateError = Color(0xFFF15515)
    val stateBgError = Color(0xFFFFD8D8)
    val stateWarning = Color(0xFFFFAA47)
    val stateBgWarning = Color(0xFFFFF3C8)

    // gray
    val gray0 = Color(0xFFFFFFFF)
    val gray5 = Color(0xFFE6E9FA)
    val gray10 = Color(0xFFD7DBF5)
    val gray15 = Color(0xFFB0B5D9)
    val gray20 = Color(0xFF9DA3CC)
    val gray25 = Color(0xFF7D84B2)
    val gray30 = Color(0xFF636B99)
    val technical = Color(0x808080)

    val transparent = Color(0x00FFFFFF)
}

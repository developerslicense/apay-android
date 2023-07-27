package kz.airbapay.apay_android.ui.resources

import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.graphics.Color

object ColorsSdk {// todo надо предоставить внешнему пользователю возможность изменить некоторые цвета
    val mainBrand = Color(0xFF319CF3)

  // block
    val bgBlock = Color(0xFFFFFFFF)
    val bgMain = Color(0xF3F4FBFF)
    val bgAccent = Color(0xFF10142D)
    val bgSecondaryAccent= Color(0xFFFFF3C8)
    val bgElements = Color(0xFFFCFCFF)

  // text
    val textMain = Color(0xFF10142D)
    val textButtonMain = mutableStateOf(Color(0xFF10142D)) //можно будет изменять
    val textLight = Color(0xFF787E9E)
    val textSecondary = Color(0xFF383E61)
    val textInversion = Color(0xFFFCFCFD)
    val textButtonInversion = mutableStateOf(Color(0xFFFCFCFD)) //можно будет изменять

  // icons
    val iconMain = Color(0xFF10142D)
    val iconSecondary = Color(0xFF787E9E)
    val iconInversion = Color(0xFFFCFCFD)

  // buttons
    val buttonMainBrand = mutableStateOf(Color(0xFF319CF3))//можно будет изменять
    val buttonSecondary = mutableStateOf(Color(0xFFF3F4FB))//можно будет изменять
    val buttonSecondaryDelete = Color(0xFFFFFBEE)
    val buttonDefault = Color(0xFFFFFFFF)

  // state
    val stateSuccess = Color(0xFF1ACE37)
    val stateBdSuccess = Color(0xFFC7F9EF)
    val stateError = Color(0xFFF15515)
    val stateBgError = Color(0xFFFFD8D8)
    val stateWarning = Color(0xFFFFAA47)
    val stateBgWarning = Color(0xFFFFF3C8)

  // sticker status
    val stickerStatusInProgress1 = Color(0xFF3399FF)
    val stickerStatusInProgress2 = Color(0xFF00CCCC)
    val stickerStatusInProgress3 = Color(0xFF00CC99)
    val stickerStatusDone = Color(0xFF33CC33)
    val stickerStatusReturned = Color(0xFFFF3333)
    val stickerStatusStop = Color(0xFFB0B5D9)
    val stickerStatusExpects = Color(0xFFFF9900)

  // gray
    val gray0 = Color(0xFFFFFFFF)
    val gray5 = Color(0xFFE6E9FA)
    val gray10 = Color(0xFFD7DBF5)
    val gray15 = Color(0xFFB0B5D9)
    val gray20 = Color(0xFF9DA3CC)
    val gray25 = Color(0xFF7D84B2)
    val gray30 = Color(0xFF636B99)
}

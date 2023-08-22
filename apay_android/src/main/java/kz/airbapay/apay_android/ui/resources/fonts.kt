package kz.airbapay.apay_android.ui.resources

import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp

val LocalFonts = staticCompositionLocalOf { Fonts() }

data class Fonts(
    val regular: TextStyle = regular(),
    val bodyRegular: TextStyle = bodyRegular(),
    val caption400: TextStyle = caption400(),
    val subtitleBold: TextStyle = subtitleBold(),
    val semiBold: TextStyle = semiBold(),
    val h0: TextStyle = h0(),
    val h1: TextStyle = h1(),
    val h2: TextStyle = h2(),
    val h3: TextStyle = h3(),
    val h4: TextStyle = h4(),
    val note: TextStyle = note(),
    val button: TextStyle = button(),
    val buttonSmall: TextStyle = buttonSmall(),
)

internal fun regular() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W400,
    fontSize = 14.sp,
    lineHeight = 22.sp
)

internal fun bodyRegular() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W400,
    fontSize = 16.sp,
    lineHeight = 24.sp
)

internal fun caption400() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W400,
    fontSize = 12.sp,
    lineHeight = 18.sp
)

internal fun subtitleBold() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W700,
    fontSize = 16.sp,
    lineHeight = 24.sp
)

internal fun semiBold() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W600,
    fontSize = 14.sp,
    lineHeight = 22.sp
)

internal fun h0() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W700,
    fontSize = 18.sp,
    lineHeight = 24.sp
)

internal fun h1() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W700,
    fontSize = 24.sp,
    lineHeight = 22.4.sp
)

internal fun h2() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W600,
    fontSize = 24.sp,
    lineHeight = 24.sp
)

internal fun h3() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W700,
    fontSize = 20.sp,
    lineHeight = 30.sp
)

internal fun h4() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W700,
    fontSize = 15.sp,
    lineHeight = 20.sp
)

internal fun note() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W600,
    fontSize = 10.sp,
    lineHeight = 12.sp
)

internal fun button() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W600,
    fontSize = 15.sp,
    lineHeight = 24.sp
)

internal fun buttonSmall() = TextStyle(
    color = ColorsSdk.textMain,
    fontFamily = regular,
    fontWeight = FontWeight.W600,
    fontSize = 13.sp,
    lineHeight = 19.5.sp
)






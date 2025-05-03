package com.github.dragon925.androidlearning.core.api.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.graphics.Color

@Immutable
data class AppTextColors(
    val color2: Color,
    val color10: Color,
    val color13: Color,
    val color16: Color,
    val color17: Color,
    val color19: Color,
    val colorPopupPlaceholder: Color
)

internal val LocalAppTextColors = staticCompositionLocalOf {
    AppTextColors(
        color2 = Color.Unspecified,
        color10 = Color.Unspecified,
        color13 = Color.Unspecified,
        color16 = Color.Unspecified,
        color17 = Color.Unspecified,
        color19 = Color.Unspecified,
        colorPopupPlaceholder = Color.Unspecified
    )
}

val macaroniAndCheese = Color(0XFFF2B630)
val warmGreyTwo = Color(0xFF757575)
val lightOliveGreen = Color(0xFF9EBF50)
val lightGreyTwo = Color(0xFFE8EDED)
val white = Color(0xFFFFFFFF)
val black70 = Color(0xB3000000)
val grey = Color(0xFF94998A)
val black12 = Color(0x1E000000)
val leaf = Color(0xFF66A636)
val blueGrey = Color(0xFF627F8F)
val grey38 = Color(0x61000000)

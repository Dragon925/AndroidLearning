package com.github.dragon925.androidlearning.core.api.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.sp
import com.github.dragon925.androidlearning.core.api.R

@Immutable
data class AppTypography(
    val textStyle2: TextStyle,
    val textStyle10: TextStyle,
    val textStyle13: TextStyle,
    val textStyle16: TextStyle,
    val textStyle17: TextStyle,
    val textStyle19: TextStyle,
    val textStylePopupPlaceholder: TextStyle
)

internal val LocalAppTypography = staticCompositionLocalOf {
    AppTypography(
        textStyle2 = TextStyle.Default,
        textStyle10 = TextStyle.Default,
        textStyle13 = TextStyle.Default,
        textStyle16 = TextStyle.Default,
        textStyle17 = TextStyle.Default,
        textStyle19 = TextStyle.Default,
        textStylePopupPlaceholder = TextStyle.Default
    )
}

internal val officinaSansFamily = FontFamily(
    Font(R.font.officina_sans_extra_bold_scc, FontWeight(800), FontStyle.Normal)
)

internal val textStyle2 = TextStyle(
    textAlign = TextAlign.Center,
    fontFamily = officinaSansFamily,
    fontStyle = FontStyle.Normal,
    fontSize = 21.sp,
)

internal val textStyle10 = TextStyle(
    textAlign = TextAlign.Center,
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 14.sp,
    lineHeight = 20.sp
)

internal val textStyle13 = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp,
)

internal val textStylePopupPlaceholder = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 16.sp,
    lineHeight = 24.sp,
)

internal val textStyle16 = TextStyle(
    textAlign = TextAlign.Center,
    fontFamily = officinaSansFamily,
    fontStyle = FontStyle.Normal,
    fontSize = 21.sp,
    lineHeight = 23.sp,
)

internal val textStyle17 = TextStyle(
    textAlign = TextAlign.Center,
    fontFamily = FontFamily.SansSerif,
    fontWeight = FontWeight.Medium,
    fontStyle = FontStyle.Normal,
    fontSize = 16.sp,
    letterSpacing = 0.04.sp,
)

internal val textStyle19 = TextStyle(
    fontFamily = FontFamily.SansSerif,
    fontStyle = FontStyle.Normal,
    fontSize = 12.sp,
    lineHeight = 16.sp
)
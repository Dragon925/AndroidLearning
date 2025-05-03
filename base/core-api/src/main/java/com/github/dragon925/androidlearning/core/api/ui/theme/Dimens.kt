package com.github.dragon925.androidlearning.core.api.ui.theme

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.staticCompositionLocalOf
import androidx.compose.ui.unit.Dp

@Immutable
data class AppDimens(
    val spacingXxs: Dp,
    val spacingXs: Dp,
    val spacingS: Dp,
    val spacingM: Dp,
    val spacingL: Dp,
    val spacingXl: Dp,
    val spacingXxl: Dp,
    val spacingXxl2: Dp
)

internal val LocalAppDimens = staticCompositionLocalOf {
    AppDimens(
        spacingXxs = Dp.Unspecified,
        spacingXs = Dp.Unspecified,
        spacingS = Dp.Unspecified,
        spacingM = Dp.Unspecified,
        spacingL = Dp.Unspecified,
        spacingXl = Dp.Unspecified,
        spacingXxl = Dp.Unspecified,
        spacingXxl2 = Dp.Unspecified,
    )
}
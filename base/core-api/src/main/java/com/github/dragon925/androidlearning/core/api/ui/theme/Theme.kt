package com.github.dragon925.androidlearning.core.api.ui.theme

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.ui.res.dimensionResource
import com.github.dragon925.androidlearning.core.api.R

private val lightScheme = lightColorScheme(
    primary = leaf,
    onPrimary = white,
    background = lightGreyTwo,
    surface = lightGreyTwo,
    surfaceContainerHighest = lightGreyTwo,
    onSurfaceVariant = black12
)

@Composable
fun AppTheme(content: @Composable () -> Unit) {
    val appTypography = AppTypography(
        textStyle2 = textStyle2,
        textStyle10 = textStyle10,
        textStyle13 = textStyle13,
        textStyle16 = textStyle16,
        textStyle17 = textStyle17,
        textStyle19 = textStyle19,
        textStylePopupPlaceholder = textStylePopupPlaceholder
    )
    val appTextColors = AppTextColors(
        color2 = white,
        color10 = black70,
        color13 = grey38,
        color16 = blueGrey,
        color17 = white,
        color19 = white,
        colorPopupPlaceholder = grey38
    )
    val appDimens = AppDimens(
        spacingXxs = dimensionResource(id = R.dimen.spacing_xxs),
        spacingXs = dimensionResource(id = R.dimen.spacing_xs),
        spacingS = dimensionResource(id = R.dimen.spacing_s),
        spacingM = dimensionResource(id = R.dimen.spacing_m),
        spacingL = dimensionResource(id = R.dimen.spacing_l),
        spacingXl = dimensionResource(id = R.dimen.spacing_xl),
        spacingXxl = dimensionResource(id = R.dimen.spacing_xxl),
        spacingXxl2 = dimensionResource(id = R.dimen.spacing_xxl_2)
    )

    CompositionLocalProvider(
        LocalAppTypography provides appTypography,
        LocalAppTextColors provides appTextColors,
        LocalAppDimens provides appDimens,
    ) {
        MaterialTheme(
            colorScheme = lightScheme,
            content = content
        )
    }
}

object AppTheme {
    val typography: AppTypography
        @Composable
        get() = LocalAppTypography.current
    val textColors: AppTextColors
        @Composable
        get() = LocalAppTextColors.current
    val dimens: AppDimens
        @Composable
        get() = LocalAppDimens.current
}
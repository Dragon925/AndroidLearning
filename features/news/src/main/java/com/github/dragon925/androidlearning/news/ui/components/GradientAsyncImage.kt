package com.github.dragon925.androidlearning.news.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import coil3.compose.AsyncImage
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.core.api.ui.theme.grey
import com.github.dragon925.androidlearning.news.ui.utils.PlaceholderPainter

@Composable
internal fun GradientAsyncImage(
    image: String?,
    modifier: Modifier = Modifier,
    contentDescription: String? = null
) {
    val placeholder = PlaceholderPainter(grey, 100f, 100f)
    var imageSize: IntSize by remember { mutableStateOf(IntSize(10, 10)) }
    Box(
        modifier.onSizeChanged { imageSize = it }
    ) {
        AsyncImage(
            model = image,
            contentDescription = contentDescription,
            modifier = Modifier
                .fillMaxWidth()
                .aspectRatio(1.5f)
                .padding(AppTheme.dimens.spacingXxs),
            placeholder = placeholder,
            error = placeholder,
            contentScale = ContentScale.Crop
        )
        Box(
            Modifier.matchParentSize()
                .background(
                    brush = Brush.radialGradient(
                        0.0f to Color(0x00EEEEEE),
                        0.5f to Color(0x00FFFFFF),
                        0.75f to Color.White,
                        center = Offset(
                            x = imageSize.width / 2f,
                            y = -imageSize.height * 0.15f
                        ),
                        radius = imageSize.width.toFloat(),
                    )
                )
        )
    }
}

@Preview
@Composable
private fun GradientAsyncImagePreview() {
    AppTheme {
        GradientAsyncImage("")
    }
}

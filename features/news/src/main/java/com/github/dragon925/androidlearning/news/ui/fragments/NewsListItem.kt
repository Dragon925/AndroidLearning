package com.github.dragon925.androidlearning.news.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import coil3.ColorImage
import coil3.annotation.ExperimentalCoilApi
import coil3.compose.AsyncImage
import coil3.compose.AsyncImagePreviewHandler
import coil3.compose.LocalAsyncImagePreviewHandler
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.core.api.ui.theme.grey
import com.github.dragon925.androidlearning.core.api.ui.theme.white
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.ui.models.NewsItem
import com.github.dragon925.androidlearning.news.ui.utils.PlaceholderPainter

@Composable
internal fun NewsListItem(newsItem: NewsItem, onClick: () -> Unit) {
    ElevatedCard(
        onClick = onClick,
        modifier = Modifier.fillMaxWidth(),
        shape = RectangleShape,
        colors = CardDefaults.elevatedCardColors(
            containerColor = white
        )
    ) {
        val placeholder = PlaceholderPainter(grey, 100f, 100f)
        var imageSize: IntSize by remember { mutableStateOf(IntSize(10, 10)) }
        Box(
            Modifier.fillMaxWidth()
                .onSizeChanged { imageSize = it }
        ) {
            AsyncImage(
                model = newsItem.image,
                contentDescription = stringResource(R.string.description_event_photo),
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

        Text(
            text = newsItem.title,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.spacingXxl2),
            style = AppTheme.typography.textStyle16.copy(
                lineBreak = LineBreak.Paragraph.copy(
                    strategy = LineBreak.Strategy.Balanced
                )
            ),
            color = AppTheme.textColors.color16
        )

        Image(
            painter = painterResource(R.drawable.bg_decor),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .padding(
                    vertical = AppTheme.dimens.spacingXs,
                    horizontal = AppTheme.dimens.spacingXl
                )
        )

        Text(
            text = newsItem.description,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = AppTheme.dimens.spacingXl),
            style = AppTheme.typography.textStyle10.copy(
                lineBreak = LineBreak.Paragraph.copy(
                    strategy = LineBreak.Strategy.Balanced
                )
            ),
            color = AppTheme.textColors.color10,
            maxLines = 3,
            overflow = TextOverflow.Ellipsis
        )

        Row(
            modifier = Modifier.fillMaxWidth()
                .padding(top = AppTheme.dimens.spacingM)
                .height(32.dp)
                .background(MaterialTheme.colorScheme.primary),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.ic_calendar),
                contentDescription = null,
                tint = MaterialTheme.colorScheme.onPrimary
            )

            Text(
                text = newsItem.date,
                style = AppTheme.typography.textStyle19,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@OptIn(ExperimentalCoilApi::class)
@Preview(showBackground = true)
@Composable
private fun NewsListItemPreview() {
    val previewHandler = AsyncImagePreviewHandler {
        ColorImage(Color.Black.toArgb())
    }
    CompositionLocalProvider(
        LocalAsyncImagePreviewHandler provides previewHandler
    ) {
        AppTheme {
            NewsListItem(
                NewsItem(
                    id = "",
                    title = "Конкурс по вокальному пению в детском доме №6",
                    description = "Дубовская школа-интернат для детей с ограниченными возможностями здоровья стала первой в области …",
                    date = "Октябрь 20, 2016"
                )
            ) { }
        }
    }
}
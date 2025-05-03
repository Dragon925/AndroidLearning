package com.github.dragon925.androidlearning.authorization.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.tooling.preview.Preview
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme

@Composable
internal fun Label(text: String) {
    Text(
        text = text,
        modifier = Modifier
            .fillMaxWidth()
            .padding(top = AppTheme.dimens.spacingXxl2),
        style = AppTheme.typography.textStyle10.copy(
            lineBreak = LineBreak.Paragraph.copy(
                strategy = LineBreak.Strategy.Balanced
            )
        ),
        color = AppTheme.textColors.color10
    )
}

@Preview
@Composable
private fun LabelPreview() {
    AppTheme {
        Label("Label")
    }
}
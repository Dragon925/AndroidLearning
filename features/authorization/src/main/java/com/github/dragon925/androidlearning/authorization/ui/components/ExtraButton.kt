package com.github.dragon925.androidlearning.authorization.ui.components

import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme

@Composable
internal fun ExtraButton(text: String, onClick: () -> Unit = {}) {
    TextButton(onClick = onClick) {
        Text(
            text = text,
            textDecoration = TextDecoration.Underline
        )
    }
}

@Preview
@Composable
private fun ExtraButtonPreview() {
    AppTheme {
        ExtraButton(text = "Click Me!")
    }
}
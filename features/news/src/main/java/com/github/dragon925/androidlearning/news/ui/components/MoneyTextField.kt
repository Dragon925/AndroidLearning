package com.github.dragon925.androidlearning.news.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import com.github.dragon925.androidlearning.core.api.ui.PlaceholderTransformation
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.news.R

@Composable
internal fun MoneyTextField(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    val style = if (value.isEmpty()) {
        AppTheme.typography.textStylePopupPlaceholder
    } else {
        LocalTextStyle.current
    }
    val textColor = if (value.isEmpty()) {
        AppTheme.textColors.colorPopupPlaceholder
    } else {
        LocalTextStyle.current.color
    }
    val visualTransformation = if (value.isEmpty()) {
        PlaceholderTransformation(stringResource(R.string.type_amount))
    } else {
        VisualTransformation.None
    }
    TextField(
        value = value.take(7),
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = style.copy(
            textAlign = TextAlign.Center
        ),
        singleLine = true,
        visualTransformation = visualTransformation,
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Number
        ),
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        )
    )
}

@Preview
@Composable
private fun MoneyTextFieldPreview() {
    AppTheme {
        MoneyTextField("", {})
    }
}
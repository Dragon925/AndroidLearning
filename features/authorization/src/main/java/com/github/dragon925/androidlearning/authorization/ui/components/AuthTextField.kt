package com.github.dragon925.androidlearning.authorization.ui.components

import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme

@Composable
internal fun AuthTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    modifier: Modifier = Modifier,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    trailingIcon: @Composable (() -> Unit)? = null,
    keyboardOptions: KeyboardOptions = KeyboardOptions(
        imeAction = ImeAction.Next
    )
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
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier,
        textStyle = style,
        label = {
            Text(
                text = label,
                style = AppTheme.typography.textStyle13,
                color = AppTheme.textColors.color13
            )
        },
        singleLine = true,
        visualTransformation = visualTransformation,
        trailingIcon = trailingIcon,
        keyboardOptions = keyboardOptions,
        colors = TextFieldDefaults.colors(
            focusedTextColor = textColor,
            unfocusedTextColor = textColor
        )
    )
}

@Preview
@Composable
private fun AuthTextFieldPreview() {
    AppTheme {
        AuthTextField(
            value = "",
            onValueChange = {},
            label = "Label",
        )
    }
}

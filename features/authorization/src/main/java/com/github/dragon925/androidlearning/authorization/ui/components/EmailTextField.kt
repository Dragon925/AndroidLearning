package com.github.dragon925.androidlearning.authorization.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.github.dragon925.androidlearning.authorization.R
import com.github.dragon925.androidlearning.authorization.ui.utils.PlaceholderTransformation
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme

@Composable
internal fun EmailTextField(email: String, onValueChange: (String) -> Unit) {
    AuthTextField(
        value = email,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = stringResource(R.string.auth_email),
        visualTransformation = if (email.isEmpty()) {
            PlaceholderTransformation(stringResource(R.string.auth_placeholder_email))
        } else {
            VisualTransformation.None
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Next,
            keyboardType = KeyboardType.Email
        )
    )
}

@Preview
@Composable
private fun EmailTextFieldPreview() {
    AppTheme {
        EmailTextField("") {}
    }
}
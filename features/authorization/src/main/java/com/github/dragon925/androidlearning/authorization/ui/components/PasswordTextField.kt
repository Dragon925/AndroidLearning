package com.github.dragon925.androidlearning.authorization.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import com.github.dragon925.androidlearning.authorization.R
import com.github.dragon925.androidlearning.core.api.ui.PlaceholderTransformation
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.core.api.ui.theme.warmGreyTwo

@Composable
internal fun PasswordTextField(password: String, onValueChange: (String) -> Unit) {
    var showPassword by rememberSaveable { mutableStateOf(false) }
    AuthTextField(
        value = password,
        onValueChange = onValueChange,
        modifier = Modifier.fillMaxWidth(),
        label = stringResource(R.string.auth_password),
        visualTransformation = when {
            password.isEmpty() -> {
                PlaceholderTransformation(stringResource(R.string.auth_placeholder_password))
            }
            !showPassword -> PasswordVisualTransformation()
            else -> VisualTransformation.None
        },
        trailingIcon = {
            PasswordToggle(showPassword) { showPassword = !showPassword }
        },
        keyboardOptions = KeyboardOptions(
            imeAction = ImeAction.Done,
            keyboardType = KeyboardType.Password
        )

    )
}

@Composable
private fun PasswordToggle(showPassword: Boolean, onClick: () -> Unit) {
    IconButton(onClick = onClick) {
        Icon(
            painter = if (showPassword) {
                painterResource(id = R.drawable.ic_hide)
            } else {
                painterResource(id = R.drawable.ic_show)
            },
            contentDescription = if (showPassword) {
                stringResource(R.string.hide_password)
            } else {
                stringResource(R.string.show_password)
            },
            tint = warmGreyTwo
        )
    }
}

@Preview
@Composable
private fun PreviewPasswordTextField() {
    AppTheme {
        PasswordTextField("") {}
    }
}
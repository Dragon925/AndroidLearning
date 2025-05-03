package com.github.dragon925.androidlearning.authorization.ui.fragments

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.viewmodel.compose.viewModel
import com.github.dragon925.androidlearning.authorization.R
import com.github.dragon925.androidlearning.authorization.domain.models.AuthState
import com.github.dragon925.androidlearning.authorization.ui.components.EmailTextField
import com.github.dragon925.androidlearning.authorization.ui.components.EnterButton
import com.github.dragon925.androidlearning.authorization.ui.components.ExtraButton
import com.github.dragon925.androidlearning.authorization.ui.components.Label
import com.github.dragon925.androidlearning.authorization.ui.components.PasswordTextField
import com.github.dragon925.androidlearning.authorization.ui.viewmodels.AuthViewModel
import com.github.dragon925.androidlearning.core.api.ui.components.TopAppBar
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme


private fun validateLoginAndPassword(login: String, password: String): Boolean {
    return login.trim().length >= 6 && password.trim().length >= 6
}


@Composable
internal fun AuthScreen(viewModel: AuthViewModel = viewModel()) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = stringResource(R.string.authorization),
                navigationIcon = {
                    IconButton(onClick = {
                        viewModel.setState(AuthState.CANCELED)
                    }) {
                        Icon(
                            painter = painterResource(com.github.dragon925.androidlearning.core.api.R.drawable.ic_back),
                            contentDescription = stringResource(com.github.dragon925.androidlearning.core.api.R.string.back)
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(
                top = innerPadding.calculateTopPadding(),
                bottom = innerPadding.calculateBottomPadding(),
                start = AppTheme.dimens.spacingL,
                end = AppTheme.dimens.spacingL
            )
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            var email: String by rememberSaveable { mutableStateOf("") }
            var password: String by rememberSaveable { mutableStateOf("") }
            val isValid: Boolean by remember {
                derivedStateOf { validateLoginAndPassword(email, password) }
            }

            Label(stringResource(R.string.external_auth_label))

            Spacer(modifier = Modifier.height(AppTheme.dimens.spacingL))

            AppIcons()

            Label(stringResource(R.string.internal_auth_label))

            Spacer(modifier = Modifier.height(AppTheme.dimens.spacingL))

            EmailTextField(email) { email = it }

            Spacer(modifier = Modifier.height(AppTheme.dimens.spacingXl))

            PasswordTextField(password) { password = it }

            Spacer(modifier = Modifier.height(AppTheme.dimens.spacingXl))

            EnterButton(
                isEnabled = isValid,
                text = stringResource(R.string.auth_enter)
            ) {
                viewModel.setState(AuthState.AUTHORIZED)
            }

            Spacer(modifier = Modifier.height(AppTheme.dimens.spacingL))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                ExtraButton(stringResource(R.string.forgot_password))

                ExtraButton(stringResource(R.string.registration))
            }
        }
    }
}

@Composable
private fun AppIcons() {
    Row(
        modifier = Modifier
            .fillMaxWidth(0.8f)
            .padding(horizontal = AppTheme.dimens.spacingXl),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        val iconSize = dimensionResource(R.dimen.avatar_size)
        Image(
            painter = painterResource(R.drawable.logo_vk),
            contentDescription = stringResource(R.string.description_auth_by_vk),
            modifier = Modifier.size(iconSize)
        )
        Image(
            painter = painterResource(R.drawable.logo_fb),
            contentDescription = stringResource(R.string.description_auth_by_fb),
            modifier = Modifier.size(iconSize)
        )
        Image(
            painter = painterResource(R.drawable.logo_ok),
            contentDescription = stringResource(R.string.description_auth_by_ok),
            modifier = Modifier.size(iconSize)
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun PreviewAuthScreen() {
    AppTheme {
        AuthScreen()
    }
}
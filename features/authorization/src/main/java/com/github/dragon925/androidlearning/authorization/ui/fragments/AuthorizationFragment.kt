package com.github.dragon925.androidlearning.authorization.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.ComposeView
import androidx.compose.ui.platform.ViewCompositionStrategy
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.text.style.LineBreak
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.tooling.preview.Preview
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import com.github.dragon925.androidlearning.authorization.R
import com.github.dragon925.androidlearning.authorization.domain.models.AuthState
import com.github.dragon925.androidlearning.authorization.ui.utils.PlaceholderTransformation
import com.github.dragon925.androidlearning.authorization.ui.viewmodels.AuthViewModel
import com.github.dragon925.androidlearning.core.api.ui.components.TopAppBar
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.core.api.ui.theme.grey
import com.github.dragon925.androidlearning.core.api.ui.theme.warmGreyTwo
import com.github.dragon925.androidlearning.core.api.R as CoreR

class AuthorizationFragment : Fragment() {

    companion object {
        private fun validateLoginAndPassword(login: String, password: String): Boolean {
            return login.trim().length >= 6 && password.trim().length >= 6
        }
    }

    private val authViewModel: AuthViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = ComposeView(requireContext()).apply {
        setViewCompositionStrategy(ViewCompositionStrategy.DisposeOnViewTreeLifecycleDestroyed)
        setContent {
            AppTheme {
                AuthScreen()
            }
        }
    }

    @Composable
    fun AuthScreen() {
        Scaffold(
            topBar = {
                TopAppBar(
                    title = stringResource(R.string.authorization),
                    navigationIcon = {
                        IconButton(onClick = {
                            authViewModel.setState(AuthState.CANCELED)
                        }) {
                            Icon(
                                painter = painterResource(CoreR.drawable.ic_back),
                                contentDescription = stringResource(CoreR.string.back)
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
                var showPassword by rememberSaveable { mutableStateOf(false) }
                val isValid: Boolean by remember {
                    derivedStateOf { validateLoginAndPassword(email, password) }
                }

                Label(stringResource(R.string.external_auth_label))

                Spacer(modifier = Modifier.height(AppTheme.dimens.spacingL))

                AppIcons()

                Label(stringResource(R.string.internal_auth_label))

                Spacer(modifier = Modifier.height(AppTheme.dimens.spacingL))

                AuthTextField(
                    value = email,
                    onValueChange = { email = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.auth_email),
                    visualTransformation = if (email.isEmpty()) {
                        PlaceholderTransformation(stringResource(R.string.auth_placeholder_email))
                    } else {
                        VisualTransformation.None
                    }
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.spacingXl))

                AuthTextField(
                    value = password,
                    onValueChange = { password = it },
                    modifier = Modifier.fillMaxWidth(),
                    label = stringResource(R.string.auth_password),
                    visualTransformation = when {
                        email.isEmpty() -> {
                            PlaceholderTransformation(stringResource(R.string.auth_placeholder_password))
                        }
                        !showPassword -> PasswordVisualTransformation()
                        else -> VisualTransformation.None
                    },
                    trailingIcon = {
                        PasswordToggle(showPassword) { showPassword = !showPassword }
                    },
                    imeAction = ImeAction.Done
                )

                Spacer(modifier = Modifier.height(AppTheme.dimens.spacingXl))

                EnterButton(
                    isEnabled = isValid,
                    text = stringResource(R.string.auth_enter)
                ) {
                    authViewModel.setState(AuthState.AUTHORIZED)
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
    private fun Label(text: String) {
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

    @Composable
    private fun AuthTextField(
        value: String,
        onValueChange: (String) -> Unit,
        modifier: Modifier = Modifier,
        label: String,
        visualTransformation: VisualTransformation = VisualTransformation.None,
        trailingIcon: @Composable (() -> Unit)? = null,
        imeAction: ImeAction = ImeAction.Next
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
            keyboardOptions = KeyboardOptions.Default.copy(
                imeAction = imeAction
            ),
            colors = TextFieldDefaults.colors(
                focusedTextColor = textColor,
                unfocusedTextColor = textColor
            )
        )
    }

    @Composable
    private fun EnterButton(isEnabled: Boolean, text: String, onClick: () -> Unit) {
        Button(
            onClick = onClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isEnabled,
            shape = MaterialTheme.shapes.extraSmall,
            colors = ButtonDefaults.buttonColors(
                containerColor = MaterialTheme.colorScheme.primary,
                disabledContainerColor = grey,
                contentColor = MaterialTheme.colorScheme.onPrimary
            )
        ) {
            Text(
                text = text,
                style = AppTheme.typography.textStyle17
            )
        }
    }

    @Composable
    private fun ExtraButton(text: String, onClick: () -> Unit = {}) {
        TextButton(onClick = onClick) {
            Text(
                text = text,
                textDecoration = TextDecoration.Underline
            )
        }
    }

    @Preview(showBackground = true)
    @Composable
    fun PreviewAuthScreen() {
        AppTheme {
            AuthScreen()
        }
    }
}
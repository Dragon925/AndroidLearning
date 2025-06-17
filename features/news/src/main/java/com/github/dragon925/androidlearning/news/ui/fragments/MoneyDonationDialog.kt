package com.github.dragon925.androidlearning.news.ui.fragments

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.window.Dialog
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.news.R
import com.github.dragon925.androidlearning.news.ui.components.MoneyChoiceSegmentedButton
import com.github.dragon925.androidlearning.news.ui.components.MoneyTextField

@Composable
internal fun MoneyDonationDialog(
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit = {},
    onDonate: (Int) -> Unit = {}
) {
    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = modifier,
            shape = RectangleShape,
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.surfaceContainerHighest
            )
        ) {
            var amount by rememberSaveable { mutableStateOf("") }
            val isValid by remember {
                derivedStateOf {
                    amount.isBlank() || amount.toIntOrNull()?.let { it in 1..9_999_999 } ?: false
                }
            }

            var selectedIndex by rememberSaveable { mutableIntStateOf(1) }
            val options = listOf(100, 500, 1000, 2000)

            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        top = AppTheme.dimens.spacingL,
                        bottom = AppTheme.dimens.spacingM,
                        start = AppTheme.dimens.spacingL,
                        end = AppTheme.dimens.spacingL
                    ),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = stringResource(R.string.donation_gratitude),
                    modifier = Modifier.fillMaxWidth(),
                    color = AppTheme.textColors.colorPopupHeader,
                    style = AppTheme.typography.textStylePopupHeader
                )

                Text(
                    text = stringResource(R.string.choose_donation_amount),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            vertical = AppTheme.dimens.spacingS
                        ),
                    color = AppTheme.textColors.colorPopupFieldTitle,
                    style = AppTheme.typography.textStylePopupFieldTitle
                )

                MoneyChoiceSegmentedButton(selectedIndex, options) { selectedIndex = it }

                Text(
                    text = stringResource(R.string.or_type_amount),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            top = AppTheme.dimens.spacingL
                        ),
                    color = AppTheme.textColors.colorPopupFieldTitle,
                    style = AppTheme.typography.textStylePopupFieldTitle
                )

                MoneyTextField(
                    value = amount,
                    onValueChange = { amount = it },
                    modifier = Modifier.fillMaxWidth()
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(
                        AppTheme.dimens.spacingXs
                    ),
                horizontalArrangement = Arrangement.End
            ) {
                TextButton(onClick = onDismiss) {
                    Text(
                        text = stringResource(R.string.cancel).uppercase(),
                        style = AppTheme.typography.textStylePopupButton
                    )
                }

                TextButton(
                    onClick = {
                        onDonate(amount.toIntOrNull() ?: options[selectedIndex])
                    },
                    enabled = isValid
                ) {
                    Text(
                        text = stringResource(R.string.transfer).uppercase(),
                        style = AppTheme.typography.textStylePopupButton
                    )
                }
            }
        }
    }
}

@Preview
@Composable
private fun MoneyDonationDialogPreview() {
    AppTheme {
        MoneyDonationDialog()
    }
}
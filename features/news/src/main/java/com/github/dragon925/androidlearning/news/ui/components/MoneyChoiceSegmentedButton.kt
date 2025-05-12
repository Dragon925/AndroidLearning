package com.github.dragon925.androidlearning.news.ui.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.github.dragon925.androidlearning.core.api.ui.theme.AppTheme
import com.github.dragon925.androidlearning.news.R

@Composable
fun MoneyChoiceSegmentedButton(
    selectedIndex: Int,
    options: List<Int>,
    onClick: (Int) -> Unit
) {
    SingleChoiceSegmentedButtonRow {
        options.forEachIndexed { index, option ->
            SegmentedButton(
                selected = selectedIndex == index,
                onClick = { onClick(index) },
                shape = SegmentedButtonDefaults.itemShape(
                    index = index,
                    count = options.size,
                    baseShape = RoundedCornerShape(0.dp)
                ),
                colors = SegmentedButtonDefaults.colors(
                    activeContainerColor = MaterialTheme.colorScheme.primary,
                    activeContentColor = MaterialTheme.colorScheme.onPrimary,
                    inactiveContainerColor = MaterialTheme.colorScheme.surface,
                    inactiveContentColor = MaterialTheme.colorScheme.primary
                ),
                border = BorderStroke(1.dp, MaterialTheme.colorScheme.primary),
                icon = {}
            ) {
                Text(
                    text = stringResource(R.string.amount, option),
                    maxLines = 1,
                    fontSize = 14.sp
                )
            }
        }
    }
}

@Preview
@Composable
fun MoneyChoiceSegmentedButtonPreview() {
    AppTheme {
        MoneyChoiceSegmentedButton(
            selectedIndex = 1,
            options = listOf(100, 200, 500, 1000),
            onClick = {}
        )
    }
}
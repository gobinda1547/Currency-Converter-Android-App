package com.gobinda.currency.converter.ui.composable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Divider
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun MenuDivider(
    height: Int = 1,
    paddingStart: Int = 0,
    paddingEnd: Int = 0,
    color: Color = MaterialTheme.colorScheme.outlineVariant
) {
    Divider(
        modifier = Modifier
            .fillMaxWidth()
            .height(height.dp)
            .padding(start = paddingStart.dp, end = paddingEnd.dp)
            .background(color = color)
    )
}
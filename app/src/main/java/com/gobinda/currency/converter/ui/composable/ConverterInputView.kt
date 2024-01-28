package com.gobinda.currency.converter.ui.composable

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp

@Composable
fun ConverterInputView(
    image: Bitmap,
    codeName: String,
    amount: String,
    onAmountChanged: (String) -> Unit,
    onCountryChangeClicked: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(color = MaterialTheme.colorScheme.background)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            modifier = Modifier
                .wrapContentSize()
                .clickable { onCountryChangeClicked.invoke() },
            verticalAlignment = Alignment.CenterVertically
        ) {
            Image(
                modifier = Modifier.width(30.dp),
                bitmap = image.asImageBitmap(),
                contentDescription = "",
            )
            Spacer(modifier = Modifier.size(16.dp))
            Text(
                text = codeName,
                color = MaterialTheme.colorScheme.onSurface,
                style = MaterialTheme.typography.titleMedium,
                maxLines = 1
            )
            Spacer(modifier = Modifier.size(8.dp))
            Icon(
                modifier = Modifier.size(20.dp),
                imageVector = Icons.Outlined.Edit,
                contentDescription = "Edit currency",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Spacer(modifier = Modifier.size(16.dp))

        Box(modifier = Modifier.fillMaxWidth()) {
            BasicTextField(
                modifier = Modifier.fillMaxWidth(),
                value = amount,
                onValueChange = onAmountChanged,
                textStyle = TextStyle(
                    color = MaterialTheme.colorScheme.onSurface,
                    fontFamily = MaterialTheme.typography.titleMedium.fontFamily,
                    fontSize = MaterialTheme.typography.titleMedium.fontSize,
                    fontStyle = MaterialTheme.typography.titleMedium.fontStyle,
                    fontWeight = MaterialTheme.typography.titleMedium.fontWeight,
                    textAlign = TextAlign.End
                ),
                cursorBrush = SolidColor(MaterialTheme.colorScheme.onSurface),
                singleLine = true,
                maxLines = 1,
                keyboardOptions = KeyboardOptions.Default.copy(keyboardType = KeyboardType.Decimal),
            )
            if (amount.isEmpty()) {
                Text(
                    modifier = Modifier.fillMaxWidth(),
                    text = "Enter amount",
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    fontFamily = MaterialTheme.typography.bodyMedium.fontFamily,
                    fontSize = MaterialTheme.typography.bodyMedium.fontSize,
                    fontStyle = MaterialTheme.typography.bodyMedium.fontStyle,
                    fontWeight = MaterialTheme.typography.bodyMedium.fontWeight,
                    textAlign = TextAlign.End
                )
            }
        }
    }
}
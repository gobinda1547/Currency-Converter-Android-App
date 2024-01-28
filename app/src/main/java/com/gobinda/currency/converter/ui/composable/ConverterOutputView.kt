package com.gobinda.currency.converter.ui.composable

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.gobinda.currency.converter.data.model.ConverterOutput

@Composable
fun ConverterOutputView(modifier: Modifier = Modifier, outputList: List<ConverterOutput>) {
    LazyColumn(modifier = modifier) {
        items(
            count = outputList.size,
            key = { i -> i }
        ) {
            ConverterSingleOutputView(
                image = outputList[it].currencyInfo.image,
                codeName = outputList[it].currencyInfo.codeName,
                amount = outputList[it].outputAmount
            )
            MenuDivider()
        }
    }
}

@Composable
fun ConverterSingleOutputView(image: Bitmap, codeName: String, amount: String) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp),
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
        Spacer(modifier = Modifier.size(16.dp))
        Text(
            modifier = Modifier.fillMaxWidth(),
            text = amount,
            color = MaterialTheme.colorScheme.onSurface,
            style = MaterialTheme.typography.bodyLarge,
            maxLines = 1,
            textAlign = TextAlign.End
        )
    }
}
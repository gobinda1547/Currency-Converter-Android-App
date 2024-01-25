package com.gobinda.currency.converter.ui.composables

import android.content.Context
import android.net.Uri
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.bumptech.glide.integration.compose.ExperimentalGlideComposeApi
import com.bumptech.glide.integration.compose.GlideImage
import com.bumptech.glide.integration.compose.placeholder
import com.gobinda.currency.converter.R
import com.gobinda.currency.converter.data.model.CurrencyInfo
import com.gobinda.currency.converter.ui.theme.CurrencyConverterTheme

@Composable
fun CurrencyWithName(currencyInfo: CurrencyInfo) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .background(MaterialTheme.colorScheme.background)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight()
                .padding(16.dp)
        ) {
            AssetGlideImage(
                assetPath = currencyInfo.imageName,
                contentDescription = "country image",
                placeholderDrawable = R.drawable.placeholder,
                errorDrawable = R.drawable.error
            )
            Spacer(modifier = Modifier.size(20.dp))
            Text(
                text = currencyInfo.fullName,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
        MenuDivider()
    }
}

@OptIn(ExperimentalGlideComposeApi::class)
@Composable
fun AssetGlideImage(
    assetPath: String,
    contentDescription: String,
    placeholderDrawable: Int,
    errorDrawable: Int,
    modifier: Modifier = Modifier,
    contentScale: ContentScale = ContentScale.Fit,
) {
    val context = LocalContext.current

    val imageUri = getImageUriFromAsset(context, assetPath)

    GlideImage(
        model = imageUri.toString(),
        contentDescription = contentDescription,
        modifier = modifier,
        contentScale = contentScale,
        loading = placeholder(resourceId = placeholderDrawable),
        failure = placeholder(resourceId = errorDrawable)
    )
}

private fun getImageUriFromAsset(context: Context, assetPath: String): Uri {
    return Uri.parse("file:///android_asset/$assetPath")
}

@Preview(showSystemUi = true)
@Composable
fun CurrencyWithNamePreview() {
    CurrencyConverterTheme {
        CurrencyWithName(
            CurrencyInfo("BDT", "Bangladeshi TK", 1.0, "bd.png", true)
        )
    }
}
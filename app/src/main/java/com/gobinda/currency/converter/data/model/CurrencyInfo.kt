package com.gobinda.currency.converter.data.model

import android.graphics.Bitmap

data class CurrencyInfo(
    val codeName: String,
    val fullName: String,
    val rate: Double,
    val image: Bitmap,
)
package com.gobinda.currency.converter.data.model

data class CurrencyInfo(
    val codeName: String,
    val fullName: String,
    val rate: Double,
    val imageName: String,
    val isImageAvailable: Boolean
)
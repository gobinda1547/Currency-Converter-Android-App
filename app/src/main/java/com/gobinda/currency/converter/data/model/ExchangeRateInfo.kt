package com.gobinda.currency.converter.data.model

data class ExchangeRateInfo(
    val disclaimer: String,
    val license: String,
    val timestamp: Long,
    val base: String,
    val rates: Map<String, Double>
)
package com.gobinda.currency.converter.ui.screen.converter

sealed class ConverterEvent {
    data class UpdateAmount(val amount: String) : ConverterEvent()
    data class UpdateCountry(val countryCode: String) : ConverterEvent()
}
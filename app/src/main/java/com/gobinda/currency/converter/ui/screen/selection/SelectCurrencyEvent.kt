package com.gobinda.currency.converter.ui.screen.selection

sealed class SelectCurrencyEvent {
    data class UpdateCurrency(val newCurrency: String) : SelectCurrencyEvent()
}
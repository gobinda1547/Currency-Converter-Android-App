package com.gobinda.currency.converter.ui.navigation

sealed class AppScreen(val route: String) {
    data object LoadingScreen : AppScreen("loading_screen")
    data object ConverterScreen : AppScreen("converter_screen")
    data object CurrencyListScreen : AppScreen("currency_list_screen")
    data object SelectCurrencyScreen : AppScreen("select_currency_screen")
}
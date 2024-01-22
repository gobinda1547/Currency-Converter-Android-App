package com.gobinda.currency.converter.ui.navigation

sealed class AppScreen(val route: String) {
    data object LoadingScreen : AppScreen("loading_screen")
    data object HomeScreen : AppScreen("home_screen")
    data object CurrencyScreen : AppScreen("currency_screen")
}
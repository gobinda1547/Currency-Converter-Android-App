package com.gobinda.currency.converter.ui.navigation

import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInHorizontally
import androidx.compose.animation.slideOutHorizontally
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.gobinda.currency.converter.ui.screen.CurrencyListScreen
import com.gobinda.currency.converter.ui.screen.LoadingScreen
import com.gobinda.currency.converter.ui.screen.converter.ConverterScreen
import com.gobinda.currency.converter.ui.screen.selection.SelectCurrencyScreen

private const val ANIMATION_OFFSET = 500
private const val ANIMATION_DURATION = 500

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = AppScreen.LoadingScreen.route
    ) {
        composable(
            route = AppScreen.LoadingScreen.route,
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeOut(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            },
        ) {
            LoadingScreen(navController = navController)
        }

        composable(
            route = AppScreen.CurrencyListScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeOut(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            }
        ) {
            CurrencyListScreen(navController = navController)
        }

        composable(
            route = AppScreen.ConverterScreen.route,
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            },
            exitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { -ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeOut(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            },
            popEnterTransition = {
                slideInHorizontally(
                    initialOffsetX = { -ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            }
        ) {
            ConverterScreen(navController = navController)
        }

        composable(
            route = AppScreen.SelectCurrencyScreen.route + "/{inputCountry}",
            enterTransition = {
                slideInHorizontally(
                    initialOffsetX = { ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeIn(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            },
            popExitTransition = {
                slideOutHorizontally(
                    targetOffsetX = { ANIMATION_OFFSET },
                    animationSpec = tween(durationMillis = ANIMATION_DURATION)
                ) + fadeOut(animationSpec = tween(durationMillis = ANIMATION_DURATION))
            }
        ) {
            SelectCurrencyScreen(navController = navController)
        }
    }
}
package com.gobinda.currency.converter.ui.theme

import android.app.Activity
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalView
import androidx.core.view.WindowCompat

private val SingleColorScheme = lightColorScheme(
    primary = appTextColor100,
    onPrimary = appBackgroundColor100,
    background = appBackgroundColor100,
    onBackground = appTextColor100,
    surface = appBackgroundColor100,
    onSurface = appTextColor100,
    surfaceVariant = itemBackgroundColor100,
    onSurfaceVariant = appTextColor100,
    outlineVariant = itemBackgroundColor50,
)

@Composable
fun CurrencyConverterTheme(content: @Composable () -> Unit) {
    val view = LocalView.current
    if (!view.isInEditMode) {
        SideEffect {
            val window = (view.context as Activity).window
            window.statusBarColor = SingleColorScheme.background.toArgb()
            window.navigationBarColor = SingleColorScheme.background.toArgb()
            WindowCompat.getInsetsController(window, view).isAppearanceLightStatusBars = false
        }
    }

    MaterialTheme(
        colorScheme = SingleColorScheme,
        typography = Typography,
        content = content
    )
}
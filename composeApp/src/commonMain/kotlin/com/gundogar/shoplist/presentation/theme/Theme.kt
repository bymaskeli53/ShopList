package com.gundogar.shoplist.presentation.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.ColorScheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable

/**
 * Platform-specific dynamic color support.
 * Returns null if dynamic colors are not supported on the current platform.
 */
@Composable
expect fun getDynamicColorScheme(darkTheme: Boolean): ColorScheme?

@Composable
fun ShopListTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = true,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        // Use dynamic colors if supported and enabled
        dynamicColor -> {
            getDynamicColorScheme(darkTheme) ?: if (darkTheme) DarkColorScheme else LightColorScheme
        }
        // Use static color schemes
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        content = content
    )
}
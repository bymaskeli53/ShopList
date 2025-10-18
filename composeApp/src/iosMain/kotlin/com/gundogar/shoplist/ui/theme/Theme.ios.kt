package com.gundogar.shoplist.ui.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getDynamicColorScheme(darkTheme: Boolean): ColorScheme? {
    // iOS doesn't support wallpaper-based dynamic colors
    // Return null to use the static color schemes
    return null
}
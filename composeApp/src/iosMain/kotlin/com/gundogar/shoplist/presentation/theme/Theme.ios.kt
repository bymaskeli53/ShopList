package com.gundogar.shoplist.presentation.theme

import androidx.compose.material3.ColorScheme
import androidx.compose.runtime.Composable

@Composable
actual fun getDynamicColorScheme(darkTheme: Boolean): ColorScheme? {
    // Dynamic colors not yet supported on iOS
    return null
}

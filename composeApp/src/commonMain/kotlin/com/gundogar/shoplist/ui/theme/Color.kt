package com.gundogar.shoplist.ui.theme

import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.ui.graphics.Color

// Dark theme colors
private val DarkPrimary = Color(0xFF00E676)
private val DarkOnPrimary = Color(0xFF000000)
private val DarkPrimaryContainer = Color(0xFF00C853)
private val DarkOnPrimaryContainer = Color(0xFF000000)

private val DarkSecondary = Color(0xFF03DAC6)
private val DarkOnSecondary = Color(0xFF000000)
private val DarkSecondaryContainer = Color(0xFF018786)
private val DarkOnSecondaryContainer = Color(0xFFFFFFFF)

private val DarkTertiary = Color(0xFFFF5252)
private val DarkOnTertiary = Color(0xFF000000)

private val DarkError = Color(0xFFCF6679)
private val DarkOnError = Color(0xFF000000)

private val DarkBackground = Color(0xFF000000)
private val DarkOnBackground = Color(0xFFFFFFFF)

private val DarkSurface = Color(0xFF1A1A1A)
private val DarkOnSurface = Color(0xFFFFFFFF)
private val DarkSurfaceVariant = Color(0xFF2A2A2A)
private val DarkOnSurfaceVariant = Color(0xFFB0B0B0)

private val DarkOutline = Color(0xFF3A3A3A)

// Light theme colors
private val LightPrimary = Color(0xFF00C853)
private val LightOnPrimary = Color(0xFFFFFFFF)
private val LightPrimaryContainer = Color(0xFFB9F6CA)
private val LightOnPrimaryContainer = Color(0xFF002106)

private val LightSecondary = Color(0xFF018786)
private val LightOnSecondary = Color(0xFFFFFFFF)
private val LightSecondaryContainer = Color(0xFFB2DFDB)
private val LightOnSecondaryContainer = Color(0xFF00201E)

private val LightTertiary = Color(0xFFD32F2F)
private val LightOnTertiary = Color(0xFFFFFFFF)

private val LightError = Color(0xFFB00020)
private val LightOnError = Color(0xFFFFFFFF)

private val LightBackground = Color(0xFFFAFAFA)
private val LightOnBackground = Color(0xFF1A1A1A)

private val LightSurface = Color(0xFFFFFFFF)
private val LightOnSurface = Color(0xFF1A1A1A)
private val LightSurfaceVariant = Color(0xFFF5F5F5)
private val LightOnSurfaceVariant = Color(0xFF424242)

private val LightOutline = Color(0xFFBDBDBD)

val DarkColorScheme = darkColorScheme(
    primary = DarkPrimary,
    onPrimary = DarkOnPrimary,
    primaryContainer = DarkPrimaryContainer,
    onPrimaryContainer = DarkOnPrimaryContainer,
    secondary = DarkSecondary,
    onSecondary = DarkOnSecondary,
    secondaryContainer = DarkSecondaryContainer,
    onSecondaryContainer = DarkOnSecondaryContainer,
    tertiary = DarkTertiary,
    onTertiary = DarkOnTertiary,
    error = DarkError,
    onError = DarkOnError,
    background = DarkBackground,
    onBackground = DarkOnBackground,
    surface = DarkSurface,
    onSurface = DarkOnSurface,
    surfaceVariant = DarkSurfaceVariant,
    onSurfaceVariant = DarkOnSurfaceVariant,
    outline = DarkOutline
)

val LightColorScheme = lightColorScheme(
    primary = LightPrimary,
    onPrimary = LightOnPrimary,
    primaryContainer = LightPrimaryContainer,
    onPrimaryContainer = LightOnPrimaryContainer,
    secondary = LightSecondary,
    onSecondary = LightOnSecondary,
    secondaryContainer = LightSecondaryContainer,
    onSecondaryContainer = LightOnSecondaryContainer,
    tertiary = LightTertiary,
    onTertiary = LightOnTertiary,
    error = LightError,
    onError = LightOnError,
    background = LightBackground,
    onBackground = LightOnBackground,
    surface = LightSurface,
    onSurface = LightOnSurface,
    surfaceVariant = LightSurfaceVariant,
    onSurfaceVariant = LightOnSurfaceVariant,
    outline = LightOutline
)
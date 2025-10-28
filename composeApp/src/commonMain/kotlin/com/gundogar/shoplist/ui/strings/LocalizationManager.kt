package com.gundogar.shoplist.ui.strings

import androidx.compose.runtime.Composable
import androidx.compose.runtime.CompositionLocalProvider
import androidx.compose.runtime.compositionLocalOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

/**
 * Composition local for accessing localized strings throughout the app.
 */
val LocalStrings = compositionLocalOf<Strings> { TurkishStrings }

/**
 * Detects the appropriate language based on system locale.
 */
private fun detectSystemLanguage(): AppLanguage {
    val systemLanguageCode = getSystemLanguageCode().lowercase()
    return when {
        systemLanguageCode.startsWith("tr") -> AppLanguage.TURKISH
        systemLanguageCode.startsWith("en") -> AppLanguage.ENGLISH
        else -> AppLanguage.ENGLISH // Default to English for unsupported languages
    }
}

/**
 * Manages the current language of the application.
 */
object LocalizationManager {
    private var isInitialized = false

    var currentLanguage by mutableStateOf(AppLanguage.TURKISH)
        private set

    val currentStrings: Strings
        get() = when (currentLanguage) {
            AppLanguage.TURKISH -> TurkishStrings
            AppLanguage.ENGLISH -> EnglishStrings
        }

    /**
     * Initializes the localization manager with system language.
     * Should be called once at app startup.
     */
    fun initialize() {
        if (!isInitialized) {
            currentLanguage = detectSystemLanguage()
            isInitialized = true
        }
    }

    /**
     * Manually sets the app language, overriding the system language.
     */
    fun setLanguage(language: AppLanguage) {
        currentLanguage = language
    }
}

/**
 * Provides localized strings to all composables in the hierarchy.
 * Automatically initializes with system language on first composition.
 * Wrap your app content with this to enable localization.
 *
 * Example:
 * ```
 * LocalizationProvider {
 *     ShopListTheme {
 *         App()
 *     }
 * }
 * ```
 */
@Composable
fun LocalizationProvider(content: @Composable () -> Unit) {
    // Initialize with system language on first composition
    androidx.compose.runtime.LaunchedEffect(Unit) {
        LocalizationManager.initialize()
    }

    CompositionLocalProvider(LocalStrings provides LocalizationManager.currentStrings) {
        content()
    }
}

package com.gundogar.shoplist.util.tts

import android.content.Context
import android.speech.tts.TextToSpeech as AndroidTTS
import java.util.Locale
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

@Suppress("EXPECT_ACTUAL_CLASSIFIERS_ARE_IN_BETA_WARNING")
actual class TextToSpeechManager actual constructor() : KoinComponent {
    private val context: Context by inject()
    private var tts: AndroidTTS? = null
    private var isInitialized = false

    init {
        // TTS will be initialized lazily when speak is first called
    }

    private fun ensureInitialized(onReady: () -> Unit) {
        if (isInitialized) {
            onReady()
            return
        }

        // Try to use Google TTS engine explicitly (supports Turkish)
        val googleTtsEngine = "com.google.android.tts"

        tts = AndroidTTS(context, { status ->
            if (status == AndroidTTS.SUCCESS) {
                val turkishLocale = Locale("tr", "TR")
                val result = tts?.setLanguage(turkishLocale)

                // Check if Turkish is available
                when (result) {
                    AndroidTTS.LANG_AVAILABLE,
                    AndroidTTS.LANG_COUNTRY_AVAILABLE,
                    AndroidTTS.LANG_COUNTRY_VAR_AVAILABLE -> {
                        println("TTS: Turkish language is available and set successfully (Engine: ${tts?.defaultEngine})")
                        isInitialized = true
                        onReady()
                    }
                    AndroidTTS.LANG_MISSING_DATA -> {
                        println("TTS: Turkish language data is missing (Engine: ${tts?.defaultEngine})")
                        // Try to use default locale as fallback
                        tts?.setLanguage(turkishLocale)
                        isInitialized = true
                        onReady()
                    }
                    AndroidTTS.LANG_NOT_SUPPORTED -> {
                        println("TTS: Turkish language is not supported by engine: ${tts?.defaultEngine}")
                        println("TTS: Please install Google Text-to-Speech from Play Store or change TTS engine in Settings")
                        // Fallback to default language
                        isInitialized = true
                        onReady()
                    }
                    else -> {
                        println("TTS: Unknown language setup result")
                        isInitialized = true
                        onReady()
                    }
                }
            } else {
                println("TTS: Initialization failed with status: $status")
            }
        }, googleTtsEngine)
    }

    actual fun speak(text: String) {
        ensureInitialized {
            tts?.speak(text, AndroidTTS.QUEUE_FLUSH, null, null)
        }
    }

    actual fun stop() {
        tts?.stop()
    }

    actual fun isSpeaking(): Boolean {
        return tts?.isSpeaking ?: false
    }

    actual fun shutdown() {
        tts?.stop()
        tts?.shutdown()
        tts = null
        isInitialized = false
    }
}

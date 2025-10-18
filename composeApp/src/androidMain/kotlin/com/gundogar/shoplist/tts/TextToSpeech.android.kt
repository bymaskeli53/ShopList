package com.gundogar.shoplist.tts

import android.speech.tts.TextToSpeech as AndroidTTS
import java.util.Locale

actual class TextToSpeechManager {
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

        // Get the application context
        val context = try {
            Class.forName("android.app.ActivityThread")
                .getMethod("currentApplication")
                .invoke(null) as android.content.Context
        } catch (e: Exception) {
            e.printStackTrace()
            return
        }

        tts = AndroidTTS(context) { status ->
            if (status == AndroidTTS.SUCCESS) {
                tts?.language = Locale("tr", "TR") // Turkish
                isInitialized = true
                onReady()
            }
        }
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

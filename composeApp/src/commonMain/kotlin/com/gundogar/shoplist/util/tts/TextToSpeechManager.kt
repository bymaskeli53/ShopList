package com.gundogar.shoplist.util.tts

/**
 * Common interface for Text-to-Speech functionality
 */
expect class TextToSpeechManager() {
    /**
     * Speak the given text
     */
    fun speak(text: String)

    /**
     * Stop speaking
     */
    fun stop()

    /**
     * Check if TTS is currently speaking
     */
    fun isSpeaking(): Boolean

    /**
     * Release resources when done
     */
    fun shutdown()
}

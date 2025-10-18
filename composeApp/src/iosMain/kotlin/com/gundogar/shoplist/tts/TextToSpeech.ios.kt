package com.gundogar.shoplist.tts

import platform.AVFAudio.AVSpeechSynthesisVoice
import platform.AVFAudio.AVSpeechSynthesizer
import platform.AVFAudio.AVSpeechUtterance

actual class TextToSpeechManager {
    private val synthesizer = AVSpeechSynthesizer()

    actual fun speak(text: String) {
        val utterance = AVSpeechUtterance.speechUtteranceWithString(text)

        // Set Turkish voice
        val turkishVoice = AVSpeechSynthesisVoice.voiceWithLanguage("tr-TR")
        if (turkishVoice != null) {
            utterance.voice = turkishVoice
        }

        // Set speech rate (0.0 - 1.0, default is ~0.5)
        utterance.rate = 0.5f

        synthesizer.speakUtterance(utterance)
    }

    actual fun stop() {
        // Just stop speaking - will stop immediately
        if (synthesizer.isSpeaking()) {
            // We'll just let the current utterance finish or manually interrupt
            // by speaking empty text
            val emptyUtterance = AVSpeechUtterance.speechUtteranceWithString("")
            synthesizer.speakUtterance(emptyUtterance)
        }
    }

    actual fun isSpeaking(): Boolean {
        return synthesizer.isSpeaking()
    }

    actual fun shutdown() {
        stop()
    }
}

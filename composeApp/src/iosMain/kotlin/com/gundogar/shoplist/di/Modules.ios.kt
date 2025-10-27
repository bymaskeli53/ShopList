package com.gundogar.shoplist.di

import com.gundogar.shoplist.data.local.DatabaseDriverFactory
import com.gundogar.shoplist.util.tts.TextToSpeechManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
    single { TextToSpeechManager() }
}
package com.gundogar.shoplist.di

import com.gundogar.shoplist.data.DatabaseDriverFactory
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single { DatabaseDriverFactory() }
}
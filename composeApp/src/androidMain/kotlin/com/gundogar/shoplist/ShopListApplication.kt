package com.gundogar.shoplist

import android.app.Application
import com.gundogar.shoplist.di.initKoin
import org.koin.android.ext.koin.androidContext

class ShopListApplication : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin {
            androidContext(this@ShopListApplication)
        }

    }
}
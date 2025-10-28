package com.gundogar.shoplist

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.gundogar.shoplist.di.initKoin

fun main() = application {
    initKoin()
    Window(
        onCloseRequest = ::exitApplication,
        title = "ShopList",
    ) {
        App()
    }
}
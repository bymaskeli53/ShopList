package com.gundogar.shoplist

import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import com.gundogar.shoplist.data.DatabaseDriverFactory

fun main() = application {
    Window(
        onCloseRequest = ::exitApplication,
        title = "ShopList",
    ) {
        App(DatabaseDriverFactory())
    }
}
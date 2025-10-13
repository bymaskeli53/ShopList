package com.gundogar.shoplist

import androidx.compose.ui.window.ComposeUIViewController
import com.gundogar.shoplist.data.DatabaseDriverFactory

fun MainViewController() = ComposeUIViewController {
    App(DatabaseDriverFactory())
}
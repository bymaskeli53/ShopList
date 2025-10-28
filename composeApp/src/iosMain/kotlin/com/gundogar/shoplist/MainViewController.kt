package com.gundogar.shoplist

import androidx.compose.ui.window.ComposeUIViewController
import com.gundogar.shoplist.di.initKoin

fun MainViewController() = ComposeUIViewController(
    configure = {
        initKoin()
    }
) {
    App()
}

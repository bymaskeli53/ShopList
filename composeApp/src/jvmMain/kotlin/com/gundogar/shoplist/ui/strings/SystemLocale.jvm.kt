package com.gundogar.shoplist.ui.strings

import java.util.Locale

actual fun getSystemLanguageCode(): String {
    return Locale.getDefault().language
}
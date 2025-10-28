package com.gundogar.shoplist.ui.strings

import android.os.Build
import java.util.Locale

actual fun getSystemLanguageCode(): String {
    val locale = if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        android.content.res.Resources.getSystem().configuration.locales[0]
    } else {
        @Suppress("DEPRECATION")
        android.content.res.Resources.getSystem().configuration.locale
    }
    return locale.language
}
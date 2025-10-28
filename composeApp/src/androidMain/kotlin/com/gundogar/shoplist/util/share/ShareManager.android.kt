package com.gundogar.shoplist.util.share

import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.widget.Toast
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject

actual class ShareManager actual constructor() : KoinComponent {
    private val context: Context by inject()

    actual fun shareToWhatsApp(text: String) {
        try {
            val packageManager = context.packageManager
            val isWhatsAppInstalled = try {
                packageManager.getPackageInfo("com.whatsapp", PackageManager.GET_ACTIVITIES)
                true
            } catch (e: PackageManager.NameNotFoundException) {
                false
            }

            if (!isWhatsAppInstalled) {
                Toast.makeText(context, "WhatsApp yüklü değil", Toast.LENGTH_SHORT).show()
                return
            }

            // Create intent to share to WhatsApp
            val intent = Intent(Intent.ACTION_SEND).apply {
                type = "text/plain"
                setPackage("com.whatsapp")
                putExtra(Intent.EXTRA_TEXT, text)
                addFlags(Intent.FLAG_ACTIVITY_NEW_TASK)
                addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)

            }

            context.startActivity(intent)
        } catch (e: Exception) {
            e.printStackTrace()
            Toast.makeText(context, "WhatsApp açılamadı", Toast.LENGTH_SHORT).show()
        }
    }
}

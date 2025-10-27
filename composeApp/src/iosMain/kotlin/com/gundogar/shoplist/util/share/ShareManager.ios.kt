package com.gundogar.shoplist.util.share

import platform.Foundation.NSURL
import platform.Foundation.NSURLComponents
import platform.Foundation.NSURLQueryItem
import platform.UIKit.UIApplication

actual class ShareManager actual constructor() {
    actual fun shareToWhatsApp(text: String) {
        // Create WhatsApp URL with text
        val urlComponents = NSURLComponents().apply {
            scheme = "whatsapp"
            host = "send"
            queryItems = listOf(
                NSURLQueryItem(name = "text", value = text)
            )
        }

        val url = urlComponents.URL

        if (url != null) {
            val application = UIApplication.sharedApplication
            if (application.canOpenURL(url)) {
                application.openURL(url)
            } else {
                println("WhatsApp is not installed")
            }
        }
    }
}

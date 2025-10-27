package com.gundogar.shoplist.util.share

/**
 * Common interface for sharing functionality
 */
expect class ShareManager() {
    /**
     * Share text content to WhatsApp
     * @param text The text content to share
     */
    fun shareToWhatsApp(text: String)
}

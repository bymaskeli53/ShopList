package com.gundogar.shoplist.ui.strings

object TurkishStrings : Strings {
    // Screen Titles
    override val screenTitleShoppingList = "Alışveriş Listem"
    override val screenTitleAddItem = "Ürün Ekle"
    override val screenTitleEditList = "Listeyi Düzenle"

    // Navigation & Actions
    override val actionBack = "Geri"
    override val actionSave = "Kaydet"
    override val actionDelete = "Sil"
    override val actionUndo = "Geri Al"
    override val actionSearch = "Ara"
    override val actionAddItem = "Ürün ekle"
    override val actionShareWhatsApp = "WhatsApp'ta paylaş"
    override val actionReadList = "Listeyi oku"

    // Form Labels
    override val labelListTitle = "Liste Başlığı"
    override val labelItemName = "Ürün Adı"
    override val labelQuantity = "Miktar"

    // Placeholders
    override val placeholderSearch = "Liste ara..."
    override val placeholderListTitle = "Örn: Haftalık Alışveriş"
    override val placeholderItemName = "Örn: Süt"
    override val placeholderQuantity = "Örn: 2 adet"

    // Messages & Instructions
    override val messageListDeleted = "Liste silindi"
    override val messageNoListsYet = "Henüz liste eklenmedi"
    override val instructionAddFirstList = "Sağ alttaki + butonuna tıklayarak ilk listenizi oluşturun"
    override val instructionAddNewItem = "Sağ üstteki + ile yeni ürün ekleyin"

    // Status & Info
    override val statusCompleted = "Tamamlandı"
    override val statusNotCompleted = "Tamamlanmadı"
    override val infoItemCount = "ürün"
    override val infoListCount = "adet listeniz var"
    override val infoSearchResults = "sonuç bulundu"

    // Text-to-Speech Content
    override val ttsListPrefix = "Alışveriş listenizde."
    override val ttsItemExists = "Bulunmaktadır"

    // Content Descriptions (Accessibility)
    override val contentDescBack = "Geri"
    override val contentDescSave = "Kaydet"
    override val contentDescDelete = "Sil"
    override val contentDescSearch = "Ara"
    override val contentDescAddItem = "Ürün ekle"
    override val contentDescShare = "WhatsApp'ta paylaş"
    override val contentDescReadList = "Listeyi oku"

    // Format Functions
    override fun formatItemCount(count: Int): String = "$count $infoItemCount"
    override fun formatListCount(count: Int): String = "$count $infoListCount"
    override fun formatSearchResults(count: Int): String = "$count $infoSearchResults"
}

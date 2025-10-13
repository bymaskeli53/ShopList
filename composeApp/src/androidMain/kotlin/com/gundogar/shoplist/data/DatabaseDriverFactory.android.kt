package com.gundogar.shoplist.data

import android.content.Context
import androidx.sqlite.db.SupportSQLiteDatabase
import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.android.AndroidSqliteDriver
import com.gundogar.shoplist.database.ShopListDatabase

actual class DatabaseDriverFactory(private val context: Context) {
    actual fun createDriver(): SqlDriver {
        return AndroidSqliteDriver(
            schema = ShopListDatabase.Schema,
            context = context,
            name = "shoplist_v4.db",  // Changed database name to force schema recreation
            callback = object : AndroidSqliteDriver.Callback(ShopListDatabase.Schema) {
                override fun onOpen(db: SupportSQLiteDatabase) {
                    super.onOpen(db)
                    // Enable foreign key constraints
                    db.execSQL("PRAGMA foreign_keys=ON")
                }
            }
        )
    }
}

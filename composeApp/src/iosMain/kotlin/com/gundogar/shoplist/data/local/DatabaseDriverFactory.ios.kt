package com.gundogar.shoplist.data.local

import app.cash.sqldelight.db.SqlDriver
import app.cash.sqldelight.driver.native.NativeSqliteDriver
import com.gundogar.shoplist.database.ShopListDatabase

actual class DatabaseDriverFactory {
    actual fun createDriver(): SqlDriver {
        return NativeSqliteDriver(ShopListDatabase.Schema, "shoplist_v4.db")
    }
}

package com.gundogar.shoplist

interface Platform {
    val name: String
}

expect fun getPlatform(): Platform
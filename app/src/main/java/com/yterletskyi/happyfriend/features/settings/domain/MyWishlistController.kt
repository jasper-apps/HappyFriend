package com.yterletskyi.happyfriend.features.settings.domain

interface MyWishlistController {
    fun isMyWishlistEnabled(): Boolean
}

class AlwaysEnabledMyWishlistController : MyWishlistController {
    override fun isMyWishlistEnabled(): Boolean = true
}
package com.yterletskyi.happyfriend.features.friends.domain

interface MyWishlistController {
    fun isMyWishlistEnabled(): Boolean
}

class AlwaysEnabledMyWishlistController : MyWishlistController {
    override fun isMyWishlistEnabled(): Boolean = true
}
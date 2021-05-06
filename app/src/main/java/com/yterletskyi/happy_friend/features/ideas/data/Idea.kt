package com.yterletskyi.happy_friend.features.ideas.data

data class Idea(
    val id: String,
    val text: String,
    val done: Boolean,
    val friendId: Long,
    val createdAt: Long
)
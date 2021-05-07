package com.yterletskyi.happy_friend.features.ideas.data

import androidx.room.*
import com.yterletskyi.happy_friend.features.friends.data.Friend

@Entity(
    foreignKeys = [
        ForeignKey(
            entity = Friend::class,
            parentColumns = arrayOf("id"),
            childColumns = arrayOf("friend_id"),
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [
        Index(name = "friend_id_index", unique = false, value = ["friend_id"])
    ]
)
data class Idea(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "done") val done: Boolean,
    @ColumnInfo(name = "friend_id") val friendId: String,
    @ColumnInfo(name = "created_at") val createdAt: Long
)
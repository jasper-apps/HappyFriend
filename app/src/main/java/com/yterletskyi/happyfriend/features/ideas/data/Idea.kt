package com.yterletskyi.happyfriend.features.ideas.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey
import com.yterletskyi.happyfriend.features.friends.data.Friend

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
        Index(name = "friend_id_index", unique = false, value = ["friend_id"]),
    ]
)
data class Idea(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "text") val text: String,
    @ColumnInfo(name = "done") val done: Boolean,
    @ColumnInfo(name = "friend_id") val friendId: String,
    @ColumnInfo(name = "created_at") val createdAt: Long,
    @ColumnInfo(name = "position") val position: Long,
)

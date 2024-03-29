package com.jasperapps.happyfriend.features.friends.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Friend(
    @PrimaryKey val id: String,
    @ColumnInfo(name = "contact_id") val contactId: Long,
    @ColumnInfo(name = "position") val position: Long,
)

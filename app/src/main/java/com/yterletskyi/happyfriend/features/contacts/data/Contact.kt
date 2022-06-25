package com.yterletskyi.happyfriend.features.contacts.data

import android.net.Uri
import java.time.LocalDate

data class Contact(
    val id: Long,
    val name: String,
    val imageUri: Uri?,
    val birthday: LocalDate?
)

val Contact.initials: String
    get() = name.split(' ').let { split ->
        when (split.size) {
            in -1..0 -> "?"
            in 0..1 -> "${split[0][0]}"
            else -> "${split[0][0]}${split[1][0]}"
        }
    }

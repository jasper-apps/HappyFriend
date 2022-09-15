package com.jasperapps.happyfriend.features.contacts.data

import android.graphics.drawable.Drawable
import java.time.LocalDate

data class Contact(
    val id: Long,
    val name: String,
    val image: Drawable?,
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

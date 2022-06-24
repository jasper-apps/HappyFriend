package com.yterletskyi.happy_friend.common

import com.yterletskyi.happy_friend.common.x.hasYear
import java.time.LocalDate

interface BirthdayFormatter {
    fun format(birthday: LocalDate?): String
}

class BirthdayFormatterImpl : BirthdayFormatter {

    override fun format(birthday: LocalDate?): String = birthday?.run {
        when {
            hasYear -> "$year-$month-$dayOfMonth"
            else -> "$month-$dayOfMonth"
        }
    }.orEmpty()
}
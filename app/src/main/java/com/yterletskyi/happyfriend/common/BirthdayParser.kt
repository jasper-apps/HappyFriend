package com.yterletskyi.happyfriend.common

import com.yterletskyi.happyfriend.common.x.LOCAL_DATE_NO_YEAR
import java.time.LocalDate
import java.time.MonthDay

/**
 * Parses a meaningful birthday [String] into a [LocalDate] object.
 */
class BirthdayParser {

    fun parse(birthdayStr: String): LocalDate? {
        return when {
            birthdayStr.matches(fullFormat) -> LocalDate.parse(birthdayStr)
            birthdayStr.matches(shortFormat) -> {
                val md = MonthDay.parse(birthdayStr)
                LocalDate.of(LOCAL_DATE_NO_YEAR, md.month, md.dayOfMonth)
            }
            else -> null
        }
    }

    private companion object {
        val fullFormat = Regex("\\d{4}-\\d{2}-\\d{2}")
        val shortFormat = Regex("--\\d{2}-\\d{2}")
    }
}

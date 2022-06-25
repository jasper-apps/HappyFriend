package com.yterletskyi.happyfriend.common

import com.yterletskyi.happyfriend.common.x.LOCAL_DATE_NO_YEAR
import java.time.LocalDate
import java.util.Locale
import junit.framework.Assert.assertEquals
import org.junit.Test

internal class LocalizedBirthdayFormatterTest {

    private val locale = Locale.US
    private val formatter = LocalizedBirthdayFormatter(locale)

    @Test
    fun testShortBirthdayFormat() {
        // GIVEN
        val birthday = LocalDate.of(LOCAL_DATE_NO_YEAR, 6, 10)

        // WHEN
        val str = formatter.format(birthday)

        // THEN
        assertEquals("10 Jun", str)
    }

    @Test
    fun testFullBirthdayFormat() {
        // GIVEN
        val birthday = LocalDate.of(1997, 12, 31)

        // WHEN
        val str = formatter.format(birthday)

        // THEN
        assertEquals("31 Dec 1997", str)
    }
}

package com.yterletskyi.happy_friend.common.x

import java.time.LocalDate

const val LOCAL_DATE_NO_YEAR: Int = 0

val LocalDate.hasYear: Boolean
    get() = year != LOCAL_DATE_NO_YEAR

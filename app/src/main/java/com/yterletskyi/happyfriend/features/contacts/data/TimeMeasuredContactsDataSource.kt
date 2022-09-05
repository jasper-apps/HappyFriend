package com.yterletskyi.happyfriend.features.contacts.data

import com.yterletskyi.happyfriend.common.logger.Logger
import com.yterletskyi.happyfriend.common.logger.loggers
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class TimeMeasuredContactsDataSource(
    private val impl: ContactsDataSource
) : ContactsDataSource by impl {

    private val logger: Logger by loggers()

    init {
        logger.info("initializing contacts data source")
    }

    override fun search(query: String) {
        logger.info("searching for <$query>")
        val time = measureTime { impl.search(query) }
        logger.info("getContacts with query=<$query> took <${time.inSeconds}> sec")
    }
}

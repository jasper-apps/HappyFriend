package com.jasperapps.happyfriend.features.contacts.data

import com.jasperapps.happyfriend.common.logger.Logger
import com.jasperapps.happyfriend.common.logger.logcatLogger
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class TimeMeasuredContactsDataSource(
    private val impl: ContactsDataSource
) : ContactsDataSource by impl {

    private val logger: Logger by logcatLogger()

    init {
        logger.info("initializing contacts data source")
    }

    override fun search(query: String) {
        logger.info("searching for <$query>")
        val time = measureTime { impl.search(query) }
        logger.info("getContacts with query=<$query> took <${time.inSeconds}> sec")
    }
}

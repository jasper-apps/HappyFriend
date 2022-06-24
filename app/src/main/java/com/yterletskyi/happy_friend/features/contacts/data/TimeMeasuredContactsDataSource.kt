package com.yterletskyi.happy_friend.features.contacts.data

import android.util.Log
import kotlin.time.ExperimentalTime
import kotlin.time.measureTime

@OptIn(ExperimentalTime::class)
class TimeMeasuredContactsDataSource(
    private val impl: ContactsDataSource
) : ContactsDataSource by impl {

    init {
        Log.i("info23", "initializing contacts data source")
    }

    override fun search(query: String) {
        Log.i("info23", "searching for <$query>")
        val time = measureTime { impl.search(query) }
        Log.i("info23", "getContacts with query=<$query> took <${time.inSeconds}> sec")
    }
}
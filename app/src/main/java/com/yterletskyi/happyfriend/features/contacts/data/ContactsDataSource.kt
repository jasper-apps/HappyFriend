package com.yterletskyi.happyfriend.features.contacts.data

import com.yterletskyi.happyfriend.common.LifecycleComponent
import kotlinx.coroutines.flow.Flow

/**
 * A phone book contacts data source.
 * This interface extends [LifecycleComponent] to make sure contacts are not queried before user
 * granted an appropriate permission. Therefore, it should be an error to call `[initialize]`
 * before the permission is obtained.
 */
interface ContactsDataSource : LifecycleComponent {
    val contactsFlow: Flow<List<Contact>>
    fun search(query: String = "")
}

package com.yterletskyi.happyfriend.features.contacts.data

import com.yterletskyi.happyfriend.common.data.FromAssetsDataSource
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DemoContactsDataSource(
    fromAssetsDataSource: FromAssetsDataSource<Contact>,
) : ContactsDataSource {

    override val contactsFlow: Flow<List<Contact>> = flowOf(
        fromAssetsDataSource.retrieve()
    )

    override fun search(query: String) {}
}
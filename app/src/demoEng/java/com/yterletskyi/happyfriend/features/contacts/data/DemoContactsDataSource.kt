package com.yterletskyi.happyfriend.features.contacts.data

import android.content.Context
import androidx.appcompat.content.res.AppCompatResources
import com.yterletskyi.happyfriend.R
import dagger.hilt.android.qualifiers.ApplicationContext
import java.time.LocalDate
import java.util.concurrent.atomic.AtomicLong
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class DemoContactsDataSource(
    @ApplicationContext private val context: Context,
) : ContactsDataSource {

    private val id = AtomicLong(1)

    override val contactsFlow: Flow<List<Contact>> = flowOf(
        listOf(
            Contact(
                id = id.getAndIncrement(),
                name = "Galen Truitt",
                image = AppCompatResources.getDrawable(context, R.drawable.ic_man_2),
                birthday = LocalDate.of(0, 3, 17)
            ),
            Contact(
                id = id.getAndIncrement(),
                name = "Carol Reed",
                image = AppCompatResources.getDrawable(context, R.drawable.ic_woman_2),
                birthday = LocalDate.of(0, 10, 24)
            ),
            Contact(
                id = id.getAndIncrement(),
                name = "Marshall Dennis",
                image = AppCompatResources.getDrawable(context, R.drawable.ic_man_3),
                birthday = null
            ),
            Contact(
                id = id.getAndIncrement(),
                name = "Edwina Anderson",
                image = AppCompatResources.getDrawable(context, R.drawable.ic_woman_3),
                birthday = null
            ),
            Contact(
                id = id.getAndIncrement(),
                name = "Bryan Weaver",
                image = AppCompatResources.getDrawable(context, R.drawable.ic_man_1),
                birthday = LocalDate.of(0, 6, 21)
            ),
            Contact(
                id = id.getAndIncrement(),
                name = "Tammy Adams",
                image = AppCompatResources.getDrawable(context, R.drawable.ic_woman_1),
                birthday = null
            ),
        )
    )

    override fun search(query: String) {}
}

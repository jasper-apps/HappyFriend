package com.yterletskyi.happyfriend.common.analytics

interface Analytics {
    fun setUserProperty(property: UserProperty<*>)

    sealed interface UserProperty<T> {
        val key: String
        val value: T

        data class NumberOfContacts(override val value: Int) : UserProperty<Int> {
            override val key: String = "contacts_count"
        }

        data class NumberOfFriends(override val value: Int) : UserProperty<Int> {
            override val key: String = "friends_count"
        }
    }
}

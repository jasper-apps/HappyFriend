package com.yterletskyi.happy_friend.features.friends.ui

import com.yterletskyi.happy_friend.R
import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemFriendBinding
import com.yterletskyi.happy_friend.features.friends.domain.FriendModelItem

class FriendsAdapterDelegate : AdapterDelegate<ItemFriendBinding>(
    ItemFriendBinding::inflate
) {

    override fun onBindViewHolder(viewHolder: Holder<ItemFriendBinding>, item: ModelItem) {
        item as FriendModelItem

        viewHolder.binding.image.setImageResource(R.drawable.ic_home_black_24dp)
        viewHolder.binding.text.text = item.fullName
        viewHolder.binding.birthday.text = item.birthday.toString()
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is FriendModelItem

    override fun getViewType(): Int = 1
}
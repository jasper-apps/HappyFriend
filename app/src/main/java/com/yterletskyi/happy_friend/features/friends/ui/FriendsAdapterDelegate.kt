package com.yterletskyi.happy_friend.features.friends.ui

import com.yterletskyi.happy_friend.common.list.AdapterDelegate
import com.yterletskyi.happy_friend.common.list.ModelItem
import com.yterletskyi.happy_friend.databinding.ItemFriendBinding
import com.yterletskyi.happy_friend.features.friends.domain.FriendModelItem

class FriendsAdapterDelegate(
    private val onItemClicked: (Int) -> Unit,
    private val onItemLongClicked: (Int) -> Unit
) : AdapterDelegate<ItemFriendBinding>(
    ItemFriendBinding::inflate
) {

    override fun onViewHolderCreated(viewHolder: Holder<ItemFriendBinding>) {
        with(viewHolder) {
            binding.root.setOnClickListener {
                onItemClicked(adapterPosition)
            }
            binding.root.setOnLongClickListener {
                onItemLongClicked(adapterPosition); true
            }
        }
    }

    override fun onBindViewHolder(viewHolder: Holder<ItemFriendBinding>, item: ModelItem) {
        item as FriendModelItem

        with(viewHolder.binding) {
            image.setImageDrawable(item.image)
            text.text = item.fullName
            birthday.text = item.birthday.toString()
        }
    }

    override fun isForViewType(item: ModelItem, position: Int): Boolean = item is FriendModelItem

    override fun getViewType(): Int = 1
}

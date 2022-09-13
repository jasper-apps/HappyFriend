package com.jasperapps.happyfriend.features.ideas.domain

import androidx.recyclerview.widget.DiffUtil
import com.jasperapps.happyfriend.common.list.ModelItem

class IdeasDiffUtil(
    private val oldList: List<ModelItem>,
    private val newList: List<ModelItem>
) : DiffUtil.Callback() {

    override fun getOldListSize(): Int = oldList.size

    override fun getNewListSize(): Int = newList.size

    override fun areItemsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old::class == new::class && when (old) {
            is IdeaModelItem -> old.id == (new as IdeaModelItem).id
            is AddIdeaModelItem -> true
            else -> throw IllegalArgumentException("Unsupported type: ${old::class}")
        }
    }

    override fun areContentsTheSame(oldItemPosition: Int, newItemPosition: Int): Boolean {
        val old = oldList[oldItemPosition]
        val new = newList[newItemPosition]

        return old::class == new::class && when (old) {
            is IdeaModelItem, AddIdeaModelItem -> true
            else -> throw IllegalArgumentException("Unsupported type: ${old::class}")
        }
    }
}

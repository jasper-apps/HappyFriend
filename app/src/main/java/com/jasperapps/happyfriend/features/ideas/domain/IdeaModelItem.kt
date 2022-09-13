package com.jasperapps.happyfriend.features.ideas.domain

import com.jasperapps.happyfriend.common.list.ModelItem
import java.util.UUID

data class IdeaModelItem(
    val id: String,
    val text: CharSequence,
    val done: Boolean,
    val focused: Boolean,
    val position: Long,
) : ModelItem {

    companion object {
        private fun empty(): IdeaModelItem {
            return IdeaModelItem(
                id = UUID.randomUUID().toString(),
                text = "",
                done = false,
                focused = false,
                position = System.currentTimeMillis(),
            )
        }

        fun withText(text: String) = empty()
            .copy(text = text)
    }
}

fun IdeaModelItem.isEmpty() = text.isEmpty()

object AddIdeaModelItem : ModelItem

package com.yterletskyi.happyfriend.features.ideas.domain

import com.yterletskyi.happyfriend.common.list.ModelItem
import java.util.UUID

data class IdeaModelItem(
    val id: String,
    val text: CharSequence,
    val done: Boolean,
    val focused: Boolean,
    val position: String,
) : ModelItem {

    companion object {
        private fun empty(): IdeaModelItem {
            val id = UUID.randomUUID().toString()
            return IdeaModelItem(
                id = id,
                text = "",
                done = false,
                focused = false,
                position = id,
            )
        }

        fun withText(text: String) = empty()
            .copy(text = text)
    }
}

fun IdeaModelItem.isEmpty() = text.isEmpty()

object AddIdeaModelItem : ModelItem

package com.yterletskyi.happyfriend.features.ideas.domain

import com.yterletskyi.happyfriend.common.list.ModelItem
import java.util.UUID

data class IdeaModelItem(
    val id: String,
    val text: CharSequence,
    val done: Boolean
) : ModelItem {

    companion object {
        fun empty() = IdeaModelItem(
            id = UUID.randomUUID().toString(),
            text = "",
            done = false
        )
    }
}

fun IdeaModelItem.isEmpty() = text.isEmpty()

object AddIdeaModelItem : ModelItem

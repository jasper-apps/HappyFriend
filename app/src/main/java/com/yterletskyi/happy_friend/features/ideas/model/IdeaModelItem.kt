package com.yterletskyi.happy_friend.features.ideas.model

import com.yterletskyi.happy_friend.common.list.ModelItem
import java.util.*

data class IdeaModelItem(
    val id: String,
    val text: String,
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
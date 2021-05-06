package com.yterletskyi.happy_friend.features.ideas.model

import com.yterletskyi.happy_friend.common.list.ModelItem

data class IdeaModelItem(
    val id: String,
    val text: String,
    val done: Boolean
) : ModelItem

object AddIdeaModelItem : ModelItem
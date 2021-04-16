package com.yterletskyi.happy_friend.common.list

interface ModelItem

object LoadingModelItem : ModelItem

object EmptyModelItem : ModelItem

class ErrorModelItem(
    val throwable: Throwable
) : ModelItem
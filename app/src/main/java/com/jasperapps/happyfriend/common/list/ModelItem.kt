package com.jasperapps.happyfriend.common.list

interface ModelItem

object LoadingModelItem : ModelItem

object EmptyModelItem : ModelItem

class ErrorModelItem(
    val throwable: Throwable
) : ModelItem

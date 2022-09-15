package com.jasperapps.happyfriend.common.data

import android.content.res.AssetManager

abstract class FromAssetsDataSource<T>(
    protected val path: String,
) {

    abstract fun retrieve(): List<T>

    protected fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName)
            .bufferedReader()
            .use { it.readText() }
}

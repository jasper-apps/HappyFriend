package com.yterletskyi.happyfriend.common.data

import android.content.res.AssetManager

class FromAssetsDataSource<T>(
    private val path: String,
    private val assets: AssetManager,
) {

    fun retrieve(): List<T> {
        val json = assets.readAssetsFile(path)
        // TODO: deserialize
        return listOf()
    }

    private fun AssetManager.readAssetsFile(fileName: String): String =
        open(fileName)
            .bufferedReader()
            .use { it.readText() }
}

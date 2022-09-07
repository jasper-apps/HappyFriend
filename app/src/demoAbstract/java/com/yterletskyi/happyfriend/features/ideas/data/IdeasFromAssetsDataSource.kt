package com.yterletskyi.happyfriend.features.ideas.data

import android.content.res.AssetManager
import com.yterletskyi.happyfriend.common.data.FromAssetsDataSource
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class IdeasFromAssetsDataSource(
    private val assetManager: AssetManager,
    jsonFilename: String,
) : FromAssetsDataSource<Idea>(jsonFilename) {

    override fun retrieve(): List<Idea> {
        val json = assetManager.readAssetsFile(path)
        return Json.decodeFromString(
            deserializer = ListSerializer(
                elementSerializer = IdeaDeserializer()
            ),
            string = json
        )
    }
}
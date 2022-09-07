package com.yterletskyi.happyfriend.features.contacts.data

import android.content.Context
import com.yterletskyi.happyfriend.common.data.FromAssetsDataSource
import kotlinx.serialization.builtins.ListSerializer
import kotlinx.serialization.json.Json

class ContactsFromAssetsDataSource(
    private val context: Context,
    jsonFilename: String,
) : FromAssetsDataSource<Contact>(jsonFilename) {

    override fun retrieve(): List<Contact> {
        val json = context.assets.readAssetsFile(path)
        return Json.decodeFromString(
            deserializer = ListSerializer(
                elementSerializer = ContactDeserializer(context)
            ),
            string = json
        )
    }
}

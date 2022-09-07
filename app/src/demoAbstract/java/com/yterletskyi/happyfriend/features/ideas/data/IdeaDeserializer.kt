package com.yterletskyi.happyfriend.features.ideas.data

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Idea::class)
class IdeaDeserializer : KSerializer<Idea> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Contact") {
        element<String>("id")
        element<String>("text")
        element<Boolean>("done", isOptional = true)
        element<String>("friend_id", isOptional = true)
        element<Long>("created_at", isOptional = true)
        element<Long>("position", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): Idea {
        return decoder.decodeStructure(descriptor) {
            var id: String? = null
            var text: String? = null
            var done: Boolean? = null
            var friendId: String? = null
            var createdAt: Long? = null
            var position: Long? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    CompositeDecoder.DECODE_DONE -> break@loop

                    0 -> id = decodeStringElement(descriptor, 0)
                    1 -> text = decodeStringElement(descriptor, 1)
                    2 -> done = decodeBooleanElement(descriptor, 2)
                    3 -> friendId = decodeStringElement(descriptor, 3)
                    4 -> createdAt = decodeLongElement(descriptor, 4)
                    5 -> position = decodeLongElement(descriptor, 5)
                    else -> throw SerializationException("Unexpected index $index")
                }
            }
            Idea(
                requireNotNull(id),
                requireNotNull(text),
                requireNotNull(done),
                requireNotNull(friendId),
                requireNotNull(createdAt),
                requireNotNull(position)
            )
        }
    }

    override fun serialize(encoder: Encoder, value: Idea) =
        throw NotImplementedError("Should not be needed")
}
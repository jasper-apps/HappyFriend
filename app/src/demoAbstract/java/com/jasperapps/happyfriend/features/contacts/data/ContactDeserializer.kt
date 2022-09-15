package com.jasperapps.happyfriend.features.contacts.data

import android.content.Context
import android.graphics.drawable.Drawable
import androidx.appcompat.content.res.AppCompatResources
import java.time.LocalDate
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.SerializationException
import kotlinx.serialization.Serializer
import kotlinx.serialization.builtins.nullable
import kotlinx.serialization.builtins.serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = Contact::class)
class ContactDeserializer(
    private val context: Context,
) : KSerializer<Contact> {

    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("Contact") {
        element<Long>("id")
        element<String>("name")
        element<String>("image", isOptional = true)
        element<String>("birthday", isOptional = true)
    }

    override fun deserialize(decoder: Decoder): Contact {
        return decoder.decodeStructure(descriptor) {
            var id: Long? = null
            var name: String? = null
            var image: Drawable? = null
            var birthday: LocalDate? = null

            loop@ while (true) {
                when (val index = decodeElementIndex(descriptor)) {
                    DECODE_DONE -> break@loop

                    0 -> id = decodeLongElement(descriptor, 0)
                    1 -> name = decodeStringElement(descriptor, 1)
                    2 -> {
                        val drawableIdStr = decodeNullableSerializableElement(
                            descriptor, 2, String.serializer().nullable, null
                        )
                        image = drawableIdStr?.let {
                            val resourceId = context.resources.getIdentifier(
                                drawableIdStr,
                                "drawable",
                                context.packageName
                            )
                            AppCompatResources.getDrawable(context, resourceId)
                        }
                    }
                    3 -> {
                        val localDateString: String? = decodeNullableSerializableElement(
                            descriptor, 3, String.serializer().nullable, null
                        )
                        birthday = localDateString?.let {
                            val parts = it.split(',')
                            LocalDate.of(parts[0].toInt(), parts[1].toInt(), parts[2].toInt())
                        }
                    }
                    else -> throw SerializationException("Unexpected index $index")
                }
            }
            Contact(
                requireNotNull(id),
                requireNotNull(name),
                image,
                birthday
            )
        }
    }

    override fun serialize(encoder: Encoder, value: Contact) =
        throw NotImplementedError("Should not be needed")
}

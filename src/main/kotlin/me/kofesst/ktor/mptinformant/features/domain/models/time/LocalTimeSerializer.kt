package me.kofesst.ktor.mptinformant.features.domain.models.time

import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.KSerializer
import kotlinx.serialization.Serializer
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.descriptors.buildClassSerialDescriptor
import kotlinx.serialization.descriptors.element
import kotlinx.serialization.encoding.CompositeDecoder.Companion.DECODE_DONE
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlinx.serialization.encoding.decodeStructure
import kotlinx.serialization.encoding.encodeStructure
import java.time.LocalTime

@OptIn(ExperimentalSerializationApi::class)
@Serializer(forClass = LocalTime::class)
object LocalTimeSerializer : KSerializer<LocalTime> {
    override val descriptor: SerialDescriptor = buildClassSerialDescriptor("LocalTime") {
        element<Int>("hour")
        element<Int>("minute")
    }

    override fun serialize(encoder: Encoder, value: LocalTime) {
        encoder.encodeStructure(descriptor) {
            encodeIntElement(descriptor, 0, value.hour)
            encodeIntElement(descriptor, 1, value.minute)
        }
    }

    override fun deserialize(decoder: Decoder): LocalTime = decoder.decodeStructure(descriptor) {
        var hour = 0
        var minute = 0

        decodingLoop@ while (true) {
            when (val index = decodeElementIndex(descriptor)) {
                DECODE_DONE -> break@decodingLoop
                0 -> hour = decodeIntElement(descriptor, index)
                1 -> minute = decodeIntElement(descriptor, index)
            }
        }

        LocalTime.of(hour, minute)
    }
}

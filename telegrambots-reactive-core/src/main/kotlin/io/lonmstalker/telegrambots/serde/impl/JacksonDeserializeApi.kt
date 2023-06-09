package io.lonmstalker.telegrambots.serde.impl

import com.fasterxml.jackson.core.type.TypeReference
import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getObjectMapper

class JacksonDeserializeApi(
    private val objectMapper: ObjectMapper = getObjectMapper()
) : DeserializeApi {

    override fun <T> deserialize(data: ByteArray, clazz: Class<T>): T = this.objectMapper.readValue(data, clazz)

    override fun <T> deserialize(data: ByteArray, type: TypeReference<T>): T = this.objectMapper.readValue(data, type)
}
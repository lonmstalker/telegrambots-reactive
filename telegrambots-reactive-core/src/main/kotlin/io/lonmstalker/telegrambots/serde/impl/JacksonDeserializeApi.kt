package io.lonmstalker.telegrambots.serde.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getObjectMapper

class JacksonDeserializeApi(
    private val objectMapper: ObjectMapper = getObjectMapper()
) : DeserializeApi {

    override fun <T> deserialize(data: ByteArray, clazz: Class<T>): T = this.objectMapper.readValue(data, clazz)
}
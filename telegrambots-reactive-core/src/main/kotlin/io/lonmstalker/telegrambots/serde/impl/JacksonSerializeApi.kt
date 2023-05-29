package io.lonmstalker.telegrambots.serde.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getObjectMapper
import io.lonmstalker.telegrambots.serde.SerializeApi

class JacksonSerializeApi @JvmOverloads constructor(
    private val objectMapper: ObjectMapper = getObjectMapper()
) : SerializeApi {

    override fun serialize(data: Any): ByteArray = this.objectMapper.writeValueAsBytes(data)
}
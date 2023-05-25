package io.lonmstalker.telegrambots.util.internal

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.serde.impl.JacksonDeserializeApi
import io.lonmstalker.telegrambots.serde.impl.JacksonSerializeApi

internal object MapperHolder {

    @Volatile
    private var objectMapper: ObjectMapper? = null

    @Volatile
    private var jacksonSerializeApi: JacksonSerializeApi? = null

    @Volatile
    private var jacksonDeserializeApi: JacksonDeserializeApi? = null

    @Synchronized
    fun getObjectMapper(): ObjectMapper =
        if (objectMapper == null) {
            ObjectMapper()
                .findAndRegisterModules()
                .apply { objectMapper = this }
        } else {
            objectMapper!!
        }

    @Synchronized
    fun getJacksonSerializeApi(objectMapper: ObjectMapper): JacksonSerializeApi =
        if (jacksonSerializeApi == null) {
            JacksonSerializeApi(objectMapper)
                .apply { jacksonSerializeApi = this }
        } else {
            jacksonSerializeApi!!
        }

    @Synchronized
    fun getJacksonDeserializeApi(objectMapper: ObjectMapper): JacksonDeserializeApi =
        if (jacksonSerializeApi == null) {
            JacksonDeserializeApi(objectMapper)
                .apply { jacksonDeserializeApi = this }
        } else {
            jacksonDeserializeApi!!
        }
}
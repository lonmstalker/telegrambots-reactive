package io.lonmstalker.telegrambots.serde

import com.fasterxml.jackson.core.type.TypeReference

interface DeserializeApi {
    fun <T> deserialize(data: ByteArray, clazz: Class<T>): T
    fun <T> deserialize(data: ByteArray, type: TypeReference<T>): T
}
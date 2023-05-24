package io.lonmstalker.telegrambots.serde

interface DeserializeApi {
    fun <T> deserialize(data: ByteArray, clazz: Class<T>): T
}
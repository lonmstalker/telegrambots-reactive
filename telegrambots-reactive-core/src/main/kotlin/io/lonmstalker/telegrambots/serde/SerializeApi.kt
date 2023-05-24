package io.lonmstalker.telegrambots.serde

interface SerializeApi {
    fun serialize(data: Any): ByteArray
}
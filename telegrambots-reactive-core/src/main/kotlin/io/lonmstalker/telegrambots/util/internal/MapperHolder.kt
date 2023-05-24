package io.lonmstalker.telegrambots.util.internal

import com.fasterxml.jackson.databind.ObjectMapper

internal object MapperHolder {

    @Volatile
    private var objectMapper: ObjectMapper? = null

    @Synchronized
    fun getObjectMapper(): ObjectMapper =
        if (objectMapper == null) {
            ObjectMapper().findAndRegisterModules()
        } else {
            objectMapper!!
        }
}
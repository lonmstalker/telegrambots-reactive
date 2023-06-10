package io.lonmstalker.telegrambots.api

/**
 * Handler of bot actions via long pooling
 */
interface LongPoolingApi {

    fun start()

    fun stop()

    fun isRunning(): Boolean
}
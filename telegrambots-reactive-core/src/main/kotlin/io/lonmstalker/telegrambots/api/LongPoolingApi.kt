package io.lonmstalker.telegrambots.api

import io.lonmstalker.telegrambots.bot.ReactiveLongPoolingBot

/**
 * Handler of bot actions via long pooling
 */
interface LongPoolingApi {

    fun addCallback(bot: ReactiveLongPoolingBot)

    fun start()

    fun stop()

    fun isRunning(): Boolean
}
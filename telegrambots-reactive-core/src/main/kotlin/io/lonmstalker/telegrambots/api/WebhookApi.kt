package io.lonmstalker.telegrambots.api

import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.WebhookBot

interface WebhookApi {

    @Throws(TelegramApiException::class)
    fun startServer()

    fun registerWebhook(bot: WebhookBot)

    fun setInternalUrl(internalUrl: String)

    @Throws(TelegramApiException::class)
    fun setKeyStore(path: String, password: String)
}
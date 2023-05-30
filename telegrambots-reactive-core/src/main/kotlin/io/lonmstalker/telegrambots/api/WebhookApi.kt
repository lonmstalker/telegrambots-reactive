package io.lonmstalker.telegrambots.api

import io.lonmstalker.telegrambots.bot.ReactiveWebhookBot
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

interface WebhookApi {

    @Throws(TelegramApiException::class)
    fun startServer()

    fun registerWebhook(bot: ReactiveWebhookBot)
}
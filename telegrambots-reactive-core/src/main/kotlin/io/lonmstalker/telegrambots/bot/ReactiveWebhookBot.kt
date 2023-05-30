package io.lonmstalker.telegrambots.bot

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramBot

interface ReactiveWebhookBot : TelegramBot {

    suspend fun onWebhookUpdateReceived(update: Update): BotApiMethod<*>?

    fun setWebhook(webHook: SetWebhook): Boolean

    fun getBotPath(): String

    val id: Int
}
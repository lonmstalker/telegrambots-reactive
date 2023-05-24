package io.lonmstalker.telegrambots.bots

import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.generics.TelegramBot
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveWebhookBot : TelegramBot {

    fun onWebhookUpdateReceived(update: Flux<Update>): Flux<BotApiMethod<*>>

    fun setWebhook(webHook: SetWebhook): Mono<Boolean>

    fun getBotPath(): String?
}
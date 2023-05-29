package io.lonmstalker.telegrambots.bot

import org.reactivestreams.Publisher
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.generics.BotOptions
import org.telegram.telegrambots.meta.generics.TelegramBot
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono

interface ReactiveLongPoolingBot : TelegramBot {

    fun onUpdateReceived(update: Flux<Update>): Publisher<Void>

    fun getOptions(): BotOptions

    @Throws(TelegramApiRequestException::class)
    fun clearWebhook(): Mono<Boolean>

    fun onClosing() {}

    val id: Int
}
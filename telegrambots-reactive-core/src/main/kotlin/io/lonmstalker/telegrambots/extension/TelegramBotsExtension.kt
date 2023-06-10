package io.lonmstalker.telegrambots.extension

import io.lonmstalker.telegrambots.bot.ReactiveAbsSender
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import reactor.core.publisher.Mono
import java.io.Serializable

inline fun <reified T : Serializable> ReactiveAbsSender.sendApiMethod(method: BotApiMethod<T>): Mono<T> =
    this.sendApiMethod(method, T::class.java)

inline fun <reified T : Serializable> ReactiveAbsSender.sendUnsafeApiMethod(method: BotApiMethod<*>): Mono<T> =
    this.sendUnsafeApiMethod(method, T::class.java)

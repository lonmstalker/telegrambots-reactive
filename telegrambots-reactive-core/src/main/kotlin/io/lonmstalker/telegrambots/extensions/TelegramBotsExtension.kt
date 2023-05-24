package io.lonmstalker.telegrambots.extensions

import io.lonmstalker.telegrambots.bots.ReactiveAbsSender
import org.reactivestreams.Publisher
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import java.io.Serializable

inline fun <reified T : Serializable, M : BotApiMethod<T>> ReactiveAbsSender.sendApiMethod(method: M): Publisher<T> =
    this.sendApiMethod(method, T::class.java)
package io.lonmstalker.telegrambots.bot.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.bot.ReactiveLongPoolingBot
import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_ERROR_REMOVE_OLD_WEBHOOK
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getBotIdIncrementer
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getJacksonDeserializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getJacksonSerializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getObjectMapper
import okhttp3.OkHttpClient
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.generics.BotOptions
import reactor.core.publisher.Mono

abstract class DefaultReactiveTelegramLongPoolingBot(
    private val botToken: String,
    private val options: DefaultBotOptions,
    httpClient: OkHttpClient,
    deserializeApi: DeserializeApi,
    serializeApi: SerializeApi
) : DefaultReactiveAbsSender(httpClient, deserializeApi, botToken, serializeApi, options),
    ReactiveLongPoolingBot {
    override var id = getBotIdIncrementer().incrementAndGet()

    constructor(httpClient: OkHttpClient, botToken: String, objectMapper: ObjectMapper) :
            this(
                botToken,
                DefaultBotOptions(),
                httpClient,
                getJacksonDeserializeApi(objectMapper),
                getJacksonSerializeApi(objectMapper)
            )

    constructor(httpClient: OkHttpClient, botToken: String) :
            this(
                botToken,
                DefaultBotOptions(),
                httpClient,
                getJacksonDeserializeApi(getObjectMapper()),
                getJacksonSerializeApi(getObjectMapper())
            )

    override fun getOptions(): BotOptions = this.options

    override fun getBotToken(): String = this.botToken

    override fun clearWebhook(): Mono<Boolean> {
        val rp = this.sendApiMethod(DeleteWebhook(), Boolean::class.java) as Mono
        return rp
            .handle { response, sink ->
                if (!response) {
                    sink.error(TelegramApiRequestException(LOG_ERROR_REMOVE_OLD_WEBHOOK))
                } else {
                    sink.next(response)
                }
            }
    }
}
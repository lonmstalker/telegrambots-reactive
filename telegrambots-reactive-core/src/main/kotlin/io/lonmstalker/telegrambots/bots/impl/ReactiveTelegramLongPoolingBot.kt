package io.lonmstalker.telegrambots.bots.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bots.DefaultBotOptions
import io.lonmstalker.telegrambots.bots.ReactiveLongPoolingBot
import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_ERROR_REMOVE_OLD_WEBHOOK
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import io.lonmstalker.telegrambots.serde.impl.JacksonDeserializeApi
import io.lonmstalker.telegrambots.serde.impl.JacksonSerializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getJacksonDeserializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getJacksonSerializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getObjectMapper
import okhttp3.OkHttpClient
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.generics.BotOptions
import reactor.core.publisher.Mono

abstract class ReactiveTelegramLongPoolingBot(
    private val botToken: () -> String,
    httpClient: OkHttpClient,
    deserializeApi: DeserializeApi,
    serializeApi: SerializeApi, options: DefaultBotOptions,
) : DefaultReactiveAbsSender(httpClient, deserializeApi, botToken, serializeApi, options),
    ReactiveLongPoolingBot {

    constructor(httpClient: OkHttpClient, botToken: () -> String, objectMapper: ObjectMapper) :
            this(
                botToken, httpClient,
                getJacksonDeserializeApi(objectMapper),
                getJacksonSerializeApi(objectMapper), DefaultBotOptions()
            )

    constructor(httpClient: OkHttpClient, botToken: () -> String) :
            this(
                botToken, httpClient,
                getJacksonDeserializeApi(getObjectMapper()),
                getJacksonSerializeApi(getObjectMapper()), DefaultBotOptions()
            )

    override fun getOptions(): BotOptions = DefaultBotOptions()

    override fun getBotToken(): String = botToken.invoke()

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
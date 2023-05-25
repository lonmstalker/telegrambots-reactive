package io.lonmstalker.telegrambots.bots.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bots.DefaultBotOptions
import io.lonmstalker.telegrambots.bots.ReactiveWebhookBot
import io.lonmstalker.telegrambots.builder.DefaultTelegramRequestBuilder
import io.lonmstalker.telegrambots.builder.WebHookRegisterBuilder
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import io.lonmstalker.telegrambots.serde.impl.JacksonDeserializeApi
import io.lonmstalker.telegrambots.serde.impl.JacksonSerializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getJacksonDeserializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getJacksonSerializeApi
import io.lonmstalker.telegrambots.util.internal.MapperHolder.getObjectMapper
import okhttp3.OkHttpClient
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import reactor.core.publisher.Mono

abstract class DefaultReactiveTelegramWebhookBot(
    private val botToken: () -> String,
    private val httpClient: OkHttpClient,
    private val options: DefaultBotOptions,
    private val deserializeApi: DeserializeApi, serializeApi: SerializeApi,
) : DefaultReactiveAbsSender(httpClient, deserializeApi, botToken, serializeApi, options), ReactiveWebhookBot {
    private val builder = WebHookRegisterBuilder(serializeApi)

    constructor(httpClient: OkHttpClient, botToken: () -> String, objectMapper: ObjectMapper) :
            this(
                botToken, httpClient, DefaultBotOptions(),
                getJacksonDeserializeApi(objectMapper), getJacksonSerializeApi(objectMapper),
            )

    constructor(httpClient: OkHttpClient, botToken: () -> String) :
            this(
                botToken, httpClient, DefaultBotOptions(),
                getJacksonDeserializeApi(getObjectMapper()), getJacksonSerializeApi(getObjectMapper())
            )

    override fun getBotToken(): String = this.botToken.invoke()

    override fun setWebhook(webHook: SetWebhook): Mono<Boolean> =
        Mono.fromCallable {
            this.httpClient.newCall(this.builder.registerWebHook(this.getBotPath(), webHook, options))
                .execute()
                .body
                ?.bytes()
                ?.let { this.deserializeApi.deserialize(it, Boolean::class.java) }
        }
}
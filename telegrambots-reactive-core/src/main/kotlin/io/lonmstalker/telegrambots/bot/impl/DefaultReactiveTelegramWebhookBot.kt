package io.lonmstalker.telegrambots.bot.impl

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.bot.ReactiveWebhookBot
import io.lonmstalker.telegrambots.builder.WebHookRegisterBuilder
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getBotIdIncrementer
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getJacksonDeserializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getJacksonSerializeApi
import io.lonmstalker.telegrambots.util.internal.InternalHolder.getObjectMapper
import okhttp3.OkHttpClient
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook

abstract class DefaultReactiveTelegramWebhookBot(
    private val botToken: String,
    private val httpClient: OkHttpClient,
    private val options: DefaultBotOptions,
    private val deserializeApi: DeserializeApi,
    serializeApi: SerializeApi,
) : DefaultReactiveAbsSender(httpClient, deserializeApi, botToken, serializeApi, options), ReactiveWebhookBot {
    override val id = getBotIdIncrementer().incrementAndGet()
    private val builder = WebHookRegisterBuilder(serializeApi)

    constructor(httpClient: OkHttpClient, botToken: String, objectMapper: ObjectMapper) :
            this(
                botToken,
                httpClient,
                DefaultBotOptions(),
                getJacksonDeserializeApi(objectMapper),
                getJacksonSerializeApi(objectMapper),
            )

    constructor(httpClient: OkHttpClient, botToken: String) :
            this(
                botToken,
                httpClient,
                DefaultBotOptions(),
                getJacksonDeserializeApi(getObjectMapper()),
                getJacksonSerializeApi(getObjectMapper())
            )

    override fun getBotToken(): String = this.botToken

    override fun setWebhook(webHook: SetWebhook): Boolean =
        this.httpClient.newCall(this.builder.registerWebHook(this.getBotPath(), webHook, options))
            .execute()
            .body
            ?.bytes()
            ?.let { this.deserializeApi.deserialize(it, Boolean::class.java) }
            ?: false
}
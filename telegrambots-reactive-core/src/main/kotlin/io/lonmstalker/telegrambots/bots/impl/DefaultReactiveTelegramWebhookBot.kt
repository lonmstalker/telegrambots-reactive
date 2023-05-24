package io.lonmstalker.telegrambots.bots.impl

import io.lonmstalker.telegrambots.bots.DefaultBotOptions
import io.lonmstalker.telegrambots.bots.ReactiveWebhookBot
import io.lonmstalker.telegrambots.builder.DefaultTelegramRequestBuilder
import io.lonmstalker.telegrambots.builder.WebHookRegisterBuilder
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.OkHttpClient
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import reactor.core.publisher.Mono

abstract class DefaultReactiveTelegramWebhookBot(
    private val httpClient: OkHttpClient,
    private val options: DefaultBotOptions,
    private val deserializeApi: DeserializeApi,
    botToken: () -> String, serializeApi: SerializeApi,
) : DefaultReactiveAbsSender(httpClient, deserializeApi, botToken, serializeApi, options), ReactiveWebhookBot {
    private val builder = WebHookRegisterBuilder(serializeApi)

    override fun setWebhook(webHook: SetWebhook): Mono<Boolean> =
        Mono.fromCallable {
            this.httpClient.newCall(this.builder.registerWebHook(this.getBotPath(), webHook, options))
                .execute()
                .body
                ?.bytes()
                ?.let { this.deserializeApi.deserialize(it, Boolean::class.java) }
        }
}
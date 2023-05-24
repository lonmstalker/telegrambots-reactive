package io.lonmstalker.telegrambots.bots.impl

import io.lonmstalker.telegrambots.bots.DefaultBotOptions
import io.lonmstalker.telegrambots.bots.ReactiveLongPoolingBot
import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_ERROR_REMOVE_OLD_WEBHOOK
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.OkHttpClient
import org.telegram.telegrambots.meta.api.methods.updates.DeleteWebhook
import org.telegram.telegrambots.meta.exceptions.TelegramApiRequestException
import org.telegram.telegrambots.meta.generics.BotOptions
import reactor.core.publisher.Mono

abstract class ReactiveTelegramLongPoolingBot(
    httpClient: OkHttpClient,
    deserializeApi: DeserializeApi,
    botToken: () -> String, serializeApi: SerializeApi, options: DefaultBotOptions,
) : DefaultReactiveAbsSender(httpClient, deserializeApi, botToken, serializeApi, options),
    ReactiveLongPoolingBot {

    override fun getOptions(): BotOptions = DefaultBotOptions()

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
package io.lonmstalker.telegrambots.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.api.impl.DefaultLongPoolingApi
import io.lonmstalker.telegrambots.bot.CustomDefaultReactiveTelegramLongPoolingBot
import io.lonmstalker.telegrambots.bot.ReactiveLongPoolingBot
import io.lonmstalker.telegrambots.bot.impl.DefaultReactiveTelegramLongPoolingBot
import io.lonmstalker.telegrambots.model.UpdateData
import io.lonmstalker.telegrambots.properties.AppProperties
import io.lonmstalker.telegrambots.register.ReactiveTelegramBotsApi
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration {

    @Bean
    fun telegramBot(
        httpClient: OkHttpClient,
        appProperties: AppProperties,
        objectMapper: ObjectMapper,
        telegramPreRegisterApi: ReactiveTelegramBotsApi
    ): DefaultReactiveTelegramLongPoolingBot =
        CustomDefaultReactiveTelegramLongPoolingBot(httpClient, appProperties, objectMapper)
            .apply { telegramPreRegisterApi.preRegisterBot(this) }

    @Bean
    fun telegramPreRegisterApi() = ReactiveTelegramBotsApi()

    @Bean
    fun botApi(
        bots: List<ReactiveLongPoolingBot>,
        httpClient: OkHttpClient,
        objectMapper: ObjectMapper,
        sink: MutableSharedFlow<UpdateData>
    ) =
        DefaultLongPoolingApi(bots, httpClient, sink, objectMapper)
            .apply { this.start() }
}

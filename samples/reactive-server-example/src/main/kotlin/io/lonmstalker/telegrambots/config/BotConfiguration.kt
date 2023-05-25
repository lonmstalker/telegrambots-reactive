package io.lonmstalker.telegrambots.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bot.CustomReactiveTelegramLongPoolingBot
import io.lonmstalker.telegrambots.bots.impl.ReactiveTelegramLongPoolingBot
import io.lonmstalker.telegrambots.properties.AppProperties
import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfiguration {

    @Bean
    fun telegramBot(
        httpClient: OkHttpClient,
        appProperties: AppProperties,
        objectMapper: ObjectMapper
    ): ReactiveTelegramLongPoolingBot =
        CustomReactiveTelegramLongPoolingBot(httpClient, appProperties, objectMapper)
}
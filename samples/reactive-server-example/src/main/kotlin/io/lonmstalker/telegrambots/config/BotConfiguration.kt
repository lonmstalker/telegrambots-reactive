package io.lonmstalker.telegrambots.config

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bot.CustomDefaultReactiveTelegramLongPoolingBot
import io.lonmstalker.telegrambots.bot.impl.DefaultReactiveTelegramLongPoolingBot
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
    ): DefaultReactiveTelegramLongPoolingBot =
        CustomDefaultReactiveTelegramLongPoolingBot(httpClient, appProperties, objectMapper)
}
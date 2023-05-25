package io.lonmstalker.telegrambots.config

import okhttp3.OkHttpClient
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class HttpClientConfiguration {

    @Bean
    fun httpClient(): OkHttpClient =
        OkHttpClient.Builder()
            .build()
}
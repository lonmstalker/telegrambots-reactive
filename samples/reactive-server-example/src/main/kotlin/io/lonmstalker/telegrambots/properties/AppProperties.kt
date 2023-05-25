package io.lonmstalker.telegrambots.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.bind.ConstructorBinding

@ConfigurationProperties(prefix = "app")
data class AppProperties @ConstructorBinding constructor(
    val bot: BotProperties
)

data class BotProperties(
    val token: String
)
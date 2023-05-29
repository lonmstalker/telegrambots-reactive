package io.lonmstalker.telegrambots.bot

import io.lonmstalker.telegrambots.constants.TelegramUrlConstants.TELEGRAM_BOT_BASE_URL
import org.telegram.telegrambots.meta.generics.BackOff
import org.telegram.telegrambots.meta.generics.BotOptions
import org.telegram.telegrambots.updatesreceivers.ExponentialBackOff

@JvmRecord
data class DefaultBotOptions @JvmOverloads constructor(
    val maxWebhookConnections: Int? = null,
    val proxyPort: Int = 0,
    val proxyHost: String? = null,
    val baseUrl: String = TELEGRAM_BOT_BASE_URL,
    val backOff: BackOff = ExponentialBackOff.Builder().build(),
    val allowedUpdates: List<String>? = null,
    val updatesTimeout: Int = 50,
    val updatesLimit: Int = 100,
    val updatesDelay: Long = 1000
) : BotOptions {
    override fun getBaseUrl(): String = this.baseUrl
}

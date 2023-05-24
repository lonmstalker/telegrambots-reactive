package io.lonmstalker.telegrambots.bots

import io.lonmstalker.telegrambots.constants.TelegramUrlConstants.TELEGRAM_BOT_BASE_URL
import org.telegram.telegrambots.meta.generics.BackOff
import org.telegram.telegrambots.meta.generics.BotOptions

@JvmRecord
data class DefaultBotOptions @JvmOverloads constructor(
    private val maxWebhookConnections: Int? = null,
    private val proxyPort: Int = 0,
    private val backOff: BackOff? = null,
    private val proxyHost: String? = null,
    private val allowedUpdates: List<String>? = null,
    private val baseUrl: String = TELEGRAM_BOT_BASE_URL,
    private val updatesTimeout: Int = 50,
    private val updatesLimit: Int = 100,
) : BotOptions {
    override fun getBaseUrl(): String = this.baseUrl
}

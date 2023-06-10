package io.lonmstalker.telegrambots.register

import io.lonmstalker.telegrambots.bot.ReactiveLongPoolingBot
import io.lonmstalker.telegrambots.bot.ReactiveWebhookBot
import io.lonmstalker.telegrambots.constants.ErrorConstants.BOT_TOKEN_OR_USERNAME_IS_INVALID
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import org.telegram.telegrambots.meta.generics.TelegramBot

/**
 * Component for pre-registration telegram bots
 */
class ReactiveTelegramBotsApi {

    fun preRegisterBot(bot: ReactiveLongPoolingBot) {
        if (!validateBotUsernameAndToken(bot)) {
            throw TelegramApiException(BOT_TOKEN_OR_USERNAME_IS_INVALID)
        }

        bot.onRegister()
        bot.clearWebhook().block()
    }

    fun preRegisterBot(bot: ReactiveWebhookBot, setWebHook: SetWebhook) {
        if (!validateBotUsernameAndToken(bot)) {
            throw TelegramApiException(BOT_TOKEN_OR_USERNAME_IS_INVALID)
        }

        bot.onRegister()
        bot.setWebhook(setWebHook)
    }

    private fun validateBotUsernameAndToken(bot: TelegramBot) =
        !bot.botToken.isNullOrEmpty() && !bot.botUsername.isNullOrEmpty()
}
package io.lonmstalker.telegrambots.constants

object ErrorConstants {
    // messages:
    const val BOT_API_ALREADY_STOPPED = "bot api already stopped"
    const val BOT_API_ALREADY_STARTED = "bot api already started"
    const val BOT_TOKEN_OR_USERNAME_IS_INVALID = "Bot token and username can't be empty"


    // logs:
    const val LOG_EMPTY_BODY_RESPONSE = "catch empty body, status: {}"
    const val LOG_ERROR_REMOVE_OLD_WEBHOOK = "Error removing old webhook"
}
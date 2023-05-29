package io.lonmstalker.telegrambots.constants

object ErrorConstants {
    // messages:
    const val BOT_API_ALREADY_STOPPED = "bot api already stopped"
    const val BOT_API_ALREADY_STARTED = "bot api already started"

    // logs:
    const val LOG_EMPTY_BODY_RESPONSE = "catch empty body, status: {}"
    const val LOG_ERROR_REMOVE_OLD_WEBHOOK = "Error removing old webhook"
}
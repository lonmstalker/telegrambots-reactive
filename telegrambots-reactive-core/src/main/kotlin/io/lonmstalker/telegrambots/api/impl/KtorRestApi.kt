package io.lonmstalker.telegrambots.api.impl

import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.lonmstalker.telegrambots.bot.ReactiveWebhookBot
import java.util.concurrent.ConcurrentHashMap

class KtorRestApi {
    private var defaultCallback: ReactiveWebhookBot? = null
    private var callbacks: ConcurrentHashMap<String, ReactiveWebhookBot>? = null

    internal fun registerCallback(callback: ReactiveWebhookBot) {
        if (defaultCallback == null && callbacks == null) {
            this.defaultCallback = callback
        } else {
            this.defaultCallback = null
            val callbacks = this.callbacks
                ?: ConcurrentHashMap<String, ReactiveWebhookBot>().also { this.callbacks = it }
            callbacks[callback.getBotPath()] = callback
        }
    }

    fun Application.api() {
        this.routing {
            post("/{botPath}") {
                val callback = getCallback(this.call.request.)
                this.call.respond(HttpStatusCode.NotFound)
            }
        }
    }

    private fun getCallback(botPath: String) =
        if (this.defaultCallback != null) {
            this.defaultCallback
        } else {
            this.callbacks!![botPath]
        }
}
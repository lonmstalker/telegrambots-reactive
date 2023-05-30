package io.lonmstalker.telegrambots.api.impl

import io.ktor.http.ContentType.Application.Json
import io.ktor.http.HttpStatusCode.Companion.BadRequest
import io.ktor.http.HttpStatusCode.Companion.InternalServerError
import io.ktor.http.HttpStatusCode.Companion.NotFound
import io.ktor.http.HttpStatusCode.Companion.OK
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.lonmstalker.telegrambots.bot.ReactiveWebhookBot
import org.telegram.telegrambots.meta.api.objects.Update
import java.util.concurrent.ConcurrentHashMap
import kotlin.collections.set

object KtorRestApi {
    private const val BOT_PATH = "botPath"
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

    fun Application.botApi() {
        this.routing { post("/{$BOT_PATH}") { botRequest(this.call) } }
    }

    private suspend fun botRequest(call: ApplicationCall) {
        val update = call.receiveNullable<Update>()
        val botPath = call.parameters[BOT_PATH] ?: return call.respond(BadRequest)

        if (update == null) {
            val response = if (this.getCallback(botPath) != null) {
                "Hi there $botPath!"
            } else {
                "Callback not found for $botPath"
            }
            call.respondText(response, status = OK, contentType = Json)
        } else {
            val callback = getCallback(botPath) ?: return call.respond(NotFound)
            try {
                val response = callback
                    .onWebhookUpdateReceived(update)
                    ?.apply { this.validate() }
                call.respondNullable(OK, response)
            } catch (ex: Exception) {
                call.respond(InternalServerError)
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
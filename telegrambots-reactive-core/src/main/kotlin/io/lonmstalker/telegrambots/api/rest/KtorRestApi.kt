package io.lonmstalker.telegrambots.api.rest

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

internal object KtorRestApi {
    private const val BOT_PATH = "botPath"
    private var defaultCallback: ReactiveWebhookBot? = null
    private var defaultCallbacks: Map<String, ReactiveWebhookBot>? = null

    fun Application.botApi(callback: ReactiveWebhookBot?, callbacks: Collection<ReactiveWebhookBot>?) {
        defaultCallback = callback
        defaultCallbacks = callbacks?.associate { it.getBotPath() to it }
        this.routing { post("/{$BOT_PATH}") { botRequest(this.call) } }
    }

    private suspend fun botRequest(call: ApplicationCall) {
        val update = call.receiveNullable<Update>()
        val botPath = call.parameters[BOT_PATH] ?: return call.respond(BadRequest)

        if (update == null) {
            val response = if (getCallback(botPath) != null) {
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
        if (defaultCallback != null) {
            defaultCallback
        } else {
            defaultCallbacks!![botPath]
        }

}
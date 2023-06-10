package io.lonmstalker.telegrambots.api.impl

import io.ktor.network.tls.certificates.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.lonmstalker.telegrambots.api.WebhookApi
import io.lonmstalker.telegrambots.api.rest.KtorRestApi.botApi
import io.lonmstalker.telegrambots.bot.ReactiveWebhookBot
import org.telegram.telegrambots.meta.exceptions.TelegramApiException
import java.io.File

/**
 * Api for Telegram web hooks
 * (!!) use only one instance, object of this class is shared
 * (!!) bots must have only one host and port, alias = webHookBot
 */
class KtorWebhookApi @JvmOverloads constructor(
    private val botPort: Int = 10240,
    private val botHost: String,
    private val keyStoreFile: String? = null,
    private val keyStorePassword: String? = null,
    private val keyPassword: String? = null
) : WebhookApi {

    init {
        if (keyStoreFile != null) {
            this.validateServerKeystoreFile(keyStoreFile)
        }
    }

    override fun startServer(callback: ReactiveWebhookBot) {
        this.startServer(callback, null)
    }

    override fun startServer(callbacks: Collection<ReactiveWebhookBot>) {
        this.startServer(null, callbacks)
    }

    private fun startServer(callback: ReactiveWebhookBot?, callbacks: Collection<ReactiveWebhookBot>?) {
        val env = applicationEngineEnvironment {
            module { botApi(callback, callbacks) }
            connector {
                this.port = botPort
                this.host = botHost
            }
            if (keyStoreFile != null) {
                sslConnector(
                    getKeyStore(),
                    keyStoreAlias,
                    keyStorePassword = { keyStorePassword?.toCharArray()!! },
                    privateKeyPassword = { keyPassword?.toCharArray() ?: keyStorePassword!!.toCharArray() },
                    {
                        this.port = 10443
                        this.keyStorePath = File(keyStoreFile)
                    }
                )
            }
        }
        embeddedServer(Netty, environment = env).start(true)
    }

    private fun getKeyStore() = buildKeyStore { }

    @Throws(TelegramApiException::class)
    private fun validateServerKeystoreFile(keyStore: String) {
        val file = File(keyStore)
        if (!file.exists() || !file.canRead()) {
            throw TelegramApiException("Can't find or access server keystore file.")
        }
    }

    companion object {
        const val keyStoreAlias = "webHookBot"
    }
}
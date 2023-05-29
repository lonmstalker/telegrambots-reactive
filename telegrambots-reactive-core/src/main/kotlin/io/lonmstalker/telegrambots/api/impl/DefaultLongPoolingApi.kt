package io.lonmstalker.telegrambots.api.impl

import io.lonmstalker.telegrambots.api.LongPoolingApi
import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.bot.ReactiveLongPoolingBot
import io.lonmstalker.telegrambots.callback.LongPoolingCallback
import io.lonmstalker.telegrambots.constants.ErrorConstants.BOT_API_ALREADY_STARTED
import io.lonmstalker.telegrambots.constants.ErrorConstants.BOT_API_ALREADY_STOPPED
import io.lonmstalker.telegrambots.constants.MediaTypeConstants.jsonType
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.GET_UPDATES
import io.lonmstalker.telegrambots.model.UpdateData
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.OkHttpClient
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.telegram.telegrambots.meta.api.methods.updates.GetUpdates
import java.util.concurrent.CopyOnWriteArrayList
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Api for Telegram long pooling
 * (!!) use only one instance, object of this class is shared
 */
class DefaultLongPoolingApi @JvmOverloads constructor(
    private val httpClient: OkHttpClient,
    private val serializeApi: SerializeApi,
    private val deserializeApi: DeserializeApi,
    private val sink: MutableSharedFlow<UpdateData>,
    private val options: DefaultBotOptions = DefaultBotOptions()
) : LongPoolingApi {

    @Volatile
    internal var lastReceivedUpdate = 0

    @Volatile
    internal var updatesDelay = this.options.updatesDelay

    private var reader: Deferred<Unit>? = null
    private val isRunning = AtomicBoolean()
    private val callbacks = CopyOnWriteArrayList<Pair<ReactiveLongPoolingBot, String>>()

    override fun addCallback(bot: ReactiveLongPoolingBot) {
        this.callbacks.add(bot to this.createBotUrl(bot.botToken))
    }

    override fun start() {
        if (this.isRunning.get()) {
            error(BOT_API_ALREADY_STARTED)
        } else {
            this.isRunning.set(true)
            this.reader = this.getReader()
        }
    }

    override fun stop() {
        if (!this.isRunning.get()) {
            error(BOT_API_ALREADY_STOPPED)
        } else {
            this.isRunning.set(false)
            this.reader?.cancel()
            this.reader = null
        }
    }

    override fun isRunning(): Boolean = this.isRunning.get()

    private fun getReader(): Deferred<Unit> =
        CoroutineScope(Dispatchers.IO).async {
            while (isRunning()) {
                coroutineScope {
                    callbacks
                        .map { launch { callTelegramApi(it) } }
                        .forEach { it.join() }
                }
                delay(updatesDelay)
            }
        }

    private fun callTelegramApi(bot: Pair<ReactiveLongPoolingBot, String>) =
        this.httpClient
            .newCall(this.getRequest(bot.second, this.getUpdatesRequest()))
            .enqueue(LongPoolingCallback(bot.first.id, this.options, this.deserializeApi, this.sink, this))

    private fun getRequest(botUrl: String, updates: GetUpdates) =
        Request
            .Builder()
            .url(botUrl)
            .post(this.serializeApi.serialize(updates).toRequestBody(jsonType))
            .build()

    private fun createBotUrl(token: String) = this.options.baseUrl + token + GET_UPDATES
    private fun getUpdatesRequest() =
        GetUpdates.builder()
            .limit(this.options.updatesLimit)
            .timeout(this.options.updatesTimeout)
            .offset(this.lastReceivedUpdate + 1)
            .apply {
                if (options.allowedUpdates != null) {
                    this.allowedUpdates(options.allowedUpdates)
                }
            }
            .build()
}
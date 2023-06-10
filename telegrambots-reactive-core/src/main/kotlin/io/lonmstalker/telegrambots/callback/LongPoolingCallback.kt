package io.lonmstalker.telegrambots.callback

import com.fasterxml.jackson.core.type.TypeReference
import io.lonmstalker.telegrambots.api.impl.DefaultLongPoolingApi
import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_EMPTY_BODY_RESPONSE
import io.lonmstalker.telegrambots.constants.LogConstants
import io.lonmstalker.telegrambots.constants.LogConstants.LOG_RESPONSE
import io.lonmstalker.telegrambots.model.UpdateData
import io.lonmstalker.telegrambots.model.telegram.TelegramResponse
import io.lonmstalker.telegrambots.serde.DeserializeApi
import kotlinx.coroutines.flow.MutableSharedFlow
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.slf4j.LoggerFactory
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.IOException

class LongPoolingCallback(
    private val botId: Int,
    private val options: DefaultBotOptions,
    private val deserializeApi: DeserializeApi,
    private val sink: MutableSharedFlow<UpdateData>,
    private val longPoolingApi: DefaultLongPoolingApi
) : Callback {
    private val responseType = object : TypeReference<TelegramResponse<List<Update>>>() {}

    override fun onFailure(call: Call, e: IOException) {
        log.error("catch telegram response error: ", e)
        this.longPoolingApi.updatesDelay = this.options.backOff.nextBackOffMillis()
    }

    override fun onResponse(call: Call, response: Response) {
        val bytes = response.body?.bytes()

        if (log.isDebugEnabled) {
            val responseTime = response.receivedResponseAtMillis - response.sentRequestAtMillis
            log.debug(LOG_RESPONSE, responseTime, response.code, bytes?.let { String(it) })
        }

        if (response.code < 500) {
            this.longPoolingApi.updatesDelay = this.options.updatesDelay
            this.options.backOff.reset()
        } else {
            this.longPoolingApi.updatesDelay = this.options.backOff.nextBackOffMillis()
        }

        bytes
            ?.let { this.processResponse(it) }
            ?: log.error(LOG_EMPTY_BODY_RESPONSE, response.code)
    }

    private fun processResponse(data: ByteArray) {
        val updates = this.deserializeApi
            .deserialize(data, this.responseType)
            .result
            .filter { it.updateId > this.longPoolingApi.lastReceivedUpdate }
            .sortedBy { it.updateId }
        this.longPoolingApi.lastReceivedUpdate = updates
            .maxOfOrNull { UpdateData(this.botId, it).also { this.sink.tryEmit(it) }.botId }
            ?: 0
        log.debug("received {} updates", updates.size)
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(LongPoolingCallback::class.java)
    }
}
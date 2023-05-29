package io.lonmstalker.telegrambots.callback

import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_EMPTY_BODY_RESPONSE
import io.lonmstalker.telegrambots.serde.DeserializeApi
import okhttp3.*
import org.slf4j.LoggerFactory
import reactor.core.publisher.MonoSink
import java.io.IOException

class OkHttpResponseCallback<T>(
    private val clazz: Class<T>,
    private val sink: MonoSink<T>,
    private val deserializeApi: DeserializeApi
) : Callback {

    override fun onFailure(call: Call, e: IOException) {
        this.sink.error(e)
    }

    override fun onResponse(call: Call, response: Response) {
        response.body
            ?.bytes()
            ?.let { this.sink.success(deserializeApi.deserialize(it, clazz)) }
            ?: this.sink.success().apply { log.error(LOG_EMPTY_BODY_RESPONSE, response.code) }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(OkHttpResponseCallback::class.java)
    }
}
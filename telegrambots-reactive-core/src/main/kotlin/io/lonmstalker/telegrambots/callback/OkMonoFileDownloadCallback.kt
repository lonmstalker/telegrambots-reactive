package io.lonmstalker.telegrambots.callback

import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_EMPTY_BODY_RESPONSE
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.slf4j.LoggerFactory
import reactor.core.publisher.MonoSink
import java.io.IOException
import java.io.InputStream

class OkMonoFileDownloadCallback(
    private val sink: MonoSink<InputStream>
) : Callback {

    override fun onFailure(call: Call, e: IOException) {
        this.sink.error(e)
    }

    override fun onResponse(call: Call, response: Response) {
        response.body
            ?.byteStream()
            ?.let { this.sink.success(it) }
            ?: this.sink.success().apply { log.error(LOG_EMPTY_BODY_RESPONSE, response.code) }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(OkMonoFileDownloadCallback::class.java)
    }
}
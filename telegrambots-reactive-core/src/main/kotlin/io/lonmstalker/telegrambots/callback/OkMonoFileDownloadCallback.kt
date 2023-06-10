package io.lonmstalker.telegrambots.callback

import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_EMPTY_BODY_RESPONSE
import io.lonmstalker.telegrambots.constants.LogConstants
import io.lonmstalker.telegrambots.constants.LogConstants.LOG_RESPONSE
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.apache.commons.io.IOUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import reactor.core.publisher.MonoSink
import java.io.IOException
import java.io.InputStream
import java.nio.charset.Charset

class OkMonoFileDownloadCallback(
    private val sink: MonoSink<InputStream>
) : Callback {

    override fun onFailure(call: Call, e: IOException) {
        this.sink.error(e)
    }

    override fun onResponse(call: Call, response: Response) {
        val byteStream = response.body?.byteStream()
        logFileDownload(log, response, byteStream)
        byteStream
            ?.let { this.sink.success(it) }
            ?: this.sink.success().apply { log.error(LOG_EMPTY_BODY_RESPONSE, response.code) }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(OkMonoFileDownloadCallback::class.java)

        internal fun logFileDownload(logger: Logger, response: Response, byteStream: InputStream?) {
            if (logger.isDebugEnabled) {
                val responseTime = response.receivedResponseAtMillis - response.sentRequestAtMillis
                logger.debug(
                    LOG_RESPONSE,
                    responseTime,
                    response.code,
                    byteStream.let { IOUtils.toString(it, Charset.defaultCharset()) })
                byteStream?.mark(Int.MAX_VALUE)
            }
        }
    }
}

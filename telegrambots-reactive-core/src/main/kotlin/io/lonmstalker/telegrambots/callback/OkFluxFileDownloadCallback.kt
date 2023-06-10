package io.lonmstalker.telegrambots.callback

import io.lonmstalker.telegrambots.callback.OkMonoFileDownloadCallback.Companion.logFileDownload
import io.lonmstalker.telegrambots.constants.ErrorConstants.LOG_EMPTY_BODY_RESPONSE
import io.lonmstalker.telegrambots.constants.LogConstants
import io.lonmstalker.telegrambots.constants.LogConstants.LOG_RESPONSE
import io.lonmstalker.telegrambots.constants.TelegramBotsConstants.DEFAULT_CHUNK_SIZE
import okhttp3.Call
import okhttp3.Callback
import okhttp3.Response
import org.apache.commons.io.IOUtils
import org.slf4j.LoggerFactory
import reactor.core.publisher.FluxSink
import java.io.IOException
import java.nio.charset.Charset
import java.util.Arrays

class OkFluxFileDownloadCallback(
    private val chunkSize: Int = DEFAULT_CHUNK_SIZE,
    private val sink: FluxSink<ByteArray>
) : Callback {
    private val dataArray = ByteArray(chunkSize)

    override fun onFailure(call: Call, e: IOException) {
        this.sink.error(e)
    }

    override fun onResponse(call: Call, response: Response) {
        val byteStream = response.body?.byteStream()

        logFileDownload(log, response, byteStream)

        if (byteStream != null) {
            if (response.body!!.contentLength() <= this.chunkSize) {
                this.sink.next(byteStream.readAllBytes())
            } else {
                while (byteStream.available() != 0) {
                    IOUtils.read(byteStream, this.dataArray)
                    this.sink.next(this.dataArray)
                    Arrays.fill(this.dataArray, 0)
                }
                this.sink.complete()
            }
        } else {
            this.sink.complete()
            log.error(LOG_EMPTY_BODY_RESPONSE, response.code)
        }
    }

    companion object {
        @JvmStatic
        private val log = LoggerFactory.getLogger(OkFluxFileDownloadCallback::class.java)
    }
}
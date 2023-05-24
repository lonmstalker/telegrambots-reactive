package io.lonmstalker.telegrambots.serde

import okhttp3.MediaType
import okhttp3.RequestBody
import okio.BufferedSink
import okio.source
import java.io.InputStream

class InputStreamRequestBody(
    private val mediaType: MediaType,
    private val inputStream: InputStream
) : RequestBody() {

    override fun contentType(): MediaType = mediaType

    override fun writeTo(sink: BufferedSink) {
        this.inputStream.source().use { sink.writeAll(it) }
    }
}
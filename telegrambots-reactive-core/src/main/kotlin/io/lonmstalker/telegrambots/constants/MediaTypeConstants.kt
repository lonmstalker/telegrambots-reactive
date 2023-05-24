package io.lonmstalker.telegrambots.constants

import okhttp3.MediaType.Companion.toMediaType

object MediaTypeConstants {
    val textType = "text/plain; charset=utf-8".toMediaType()
    val jsonType = "application/json; charset=utf-8".toMediaType()
    val multipartType = "multipart/form-data; charset=utf-8".toMediaType()
    val octetStreamType = "application/octet-stream; charset=utf-8".toMediaType()
}
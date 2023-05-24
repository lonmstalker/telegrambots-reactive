package io.lonmstalker.telegrambots.extensions.internal

import io.lonmstalker.telegrambots.constants.MediaTypeConstants.octetStreamType
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.PNG_STICKER
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.TGS_STICKER
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.THUMB
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.WEBM_STICKER
import io.lonmstalker.telegrambots.serde.InputStreamRequestBody
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.media.*

internal fun MultipartBody.Builder.addInputData(
    mediaField: String,
    media: List<InputMedia>,
    serializeApi: SerializeApi,
    addField: Boolean = true
): MultipartBody.Builder =
    media
        .forEach { this.addInputData(mediaField, it, addField, serializeApi) }
        .let { this }

internal fun MultipartBody.Builder.addInputData(
    mediaField: String,
    media: InputMedia,
    addField: Boolean,
    serializeApi: SerializeApi
): MultipartBody.Builder {
    if (media.isNewMedia) {
        if (media.newMediaFile != null) {
            this.addFormDataPart(media.mediaName, media.mediaName, media.newMediaFile.asRequestBody(octetStreamType))
        } else if (media.newMediaStream != null) {
            this.addFormDataPart(
                media.mediaName,
                media.mediaName,
                InputStreamRequestBody(octetStreamType, media.newMediaStream)
            )
        }
    }
    when (media) {
        is InputMediaAudio -> this.addFile(THUMB, media.thumb, false)
        is InputMediaDocument -> this.addFile(THUMB, media.thumb, false)
        is InputMediaVideo -> this.addFile(THUMB, media.thumb, false)
        is InputMediaAnimation -> this.addFile(THUMB, media.thumb, false)
    }
    if (addField) {
        this.addSerializePart(mediaField, media, serializeApi)
    }
    return this
}

internal fun MultipartBody.Builder.addSticker(pngSticker: InputFile?, tgsSticker: InputFile?, webmSticker: InputFile?) =
    if (pngSticker != null) {
        this.addFile(PNG_STICKER, pngSticker)
    } else if (tgsSticker != null) {
        this.addFile(TGS_STICKER, tgsSticker)
    } else {
        this.addFile(WEBM_STICKER, webmSticker!!)
    }
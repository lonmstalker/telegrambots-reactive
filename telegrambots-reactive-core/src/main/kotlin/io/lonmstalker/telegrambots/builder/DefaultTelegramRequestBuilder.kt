package io.lonmstalker.telegrambots.builder

import io.lonmstalker.telegrambots.bots.DefaultBotOptions
import io.lonmstalker.telegrambots.constants.MediaTypeConstants.jsonType
import io.lonmstalker.telegrambots.constants.MediaTypeConstants.multipartType
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.AUDIT
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.DOCUMENT
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.MEDIA
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.PHOTO
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.STICKER
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.VIDEO
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.VIDEO_NOTE
import io.lonmstalker.telegrambots.constants.MethodTypeConstants.VOICE
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.ALLOW_SENDING_WITHOUT_REPLY
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.ANIMATION
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.CAPTION
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.CAPTION_ENTITIES
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.CHAT_ID
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.DISABLE_CONTENT_TYPE_DETECTION
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.DISABLE_NOTIFICATION
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.DURATION
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.EMOJIS
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.HAS_SPOILER
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.HEIGHT
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.INLINE_MESSAGE_ID
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.LENGTH
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.MASK_POSITION
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.MESSAGE_ID
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.MESSAGE_THREAD_ID
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.NAME
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.PARSE_MODE
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.PERFORMER
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.PNG_STICKER
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.PROTECT_CONTENT
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.REPLY_MARKUP
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.REPLY_TO_MESSAGE_ID
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.STICKER_TYPE
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.SUPPORTS_STREAMING
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.THUMB
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.TITLE
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.USER_ID
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.WIDTH
import io.lonmstalker.telegrambots.constants.TelegramBotsConstants.SLASH
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_ANIMATION
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_AUDIO
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_CHAT_PHOTO
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_CREATE_NEW_STICKER_SET
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_DOCUMENT
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_EDIT_MESSAGE_MEDIA
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_MEDIA_GROUP
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_PHOTO
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_SET_STICKER_SET_THUMB
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_STICKER
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_STICKER_TO_SET
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_UPLOAD_STICKER_FILE
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_VIDEO
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_VIDEO_NOTE
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_VOICE
import io.lonmstalker.telegrambots.extensions.internal.*
import io.lonmstalker.telegrambots.extensions.internal.addFile
import io.lonmstalker.telegrambots.extensions.internal.addPart
import io.lonmstalker.telegrambots.extensions.internal.addRelatedPart
import io.lonmstalker.telegrambots.extensions.internal.addSerializePart
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.MultipartBody
import okhttp3.Request
import okhttp3.RequestBody.Companion.toRequestBody
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia
import java.io.Serializable

class DefaultTelegramRequestBuilder(
    private val botToken: () -> String,
    private val serializeApi: SerializeApi,
    private val options: DefaultBotOptions
) {
    private val baseUrl = this.options.baseUrl + this.botToken.invoke() + SLASH

    fun execute(method: SendDocument): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(DOCUMENT, method.document)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addRelatedPart(CAPTION, method.caption, PARSE_MODE, method.parseMode)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addPart(DISABLE_CONTENT_TYPE_DETECTION, method.disableContentTypeDetection)
            .addSerializePart(CAPTION_ENTITIES, method.captionEntities, this.serializeApi)
            .addFile(THUMB, method.thumb, false)
            .addPart(THUMB, method.thumb?.attachName)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_DOCUMENT).post(it).build() }

    fun execute(method: SendPhoto): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(PHOTO, method.photo)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addRelatedPart(CAPTION, method.caption, PARSE_MODE, method.parseMode)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addSerializePart(CAPTION_ENTITIES, method.captionEntities, this.serializeApi)
            .addPart(HAS_SPOILER, method.hasSpoiler)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_PHOTO).post(it).build() }

    fun execute(method: SendVideo): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(VIDEO, method.video)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addRelatedPart(CAPTION, method.caption, PARSE_MODE, method.parseMode)
            .addPart(SUPPORTS_STREAMING, method.supportsStreaming)
            .addPart(DURATION, method.duration)
            .addPart(WIDTH, method.width)
            .addPart(HEIGHT, method.height)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addFile(THUMB, method.thumb, false)
            .addPart(THUMB, method.thumb?.attachName)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addSerializePart(CAPTION_ENTITIES, method.captionEntities, this.serializeApi)
            .addPart(HAS_SPOILER, method.hasSpoiler)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_VIDEO).post(it).build() }

    fun execute(method: SendVideoNote): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(VIDEO_NOTE, method.videoNote)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addPart(DURATION, method.duration)
            .addPart(LENGTH, method.length)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addFile(THUMB, method.thumb, false)
            .addPart(THUMB, method.thumb?.attachName)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_VIDEO_NOTE).post(it).build() }

    fun execute(method: SendSticker): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(STICKER, method.sticker)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_STICKER).post(it).build() }

    fun execute(method: SendAudio): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(AUDIT, method.audio)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addPart(PERFORMER, method.performer)
            .addPart(TITLE, method.title)
            .addPart(DURATION, method.duration)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addRelatedPart(CAPTION, method.caption, PARSE_MODE, method.parseMode)
            .addFile(THUMB, method.thumb, false)
            .addPart(THUMB, method.thumb?.attachName)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addSerializePart(CAPTION_ENTITIES, method.captionEntities, this.serializeApi)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_AUDIO).post(it).build() }

    fun execute(method: SendVoice): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(VOICE, method.voice)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(DURATION, method.duration)
            .addRelatedPart(CAPTION, method.caption, PARSE_MODE, method.parseMode)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addSerializePart(CAPTION_ENTITIES, method.captionEntities, this.serializeApi)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_VOICE).post(it).build() }

    fun execute(method: SendMediaGroup): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addInputData(MEDIA, method.medias, this.serializeApi, false)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_MEDIA_GROUP).post(it).build() }

    fun execute(method: SetChatPhoto): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(PHOTO, method.photo)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_CHAT_PHOTO).post(it).build() }

    fun execute(method: AddStickerToSet): Request =
        MultipartBody.Builder()
            .addPart(USER_ID, method.userId)
            .addPart(NAME, method.name)
            .addPart(EMOJIS, method.emojis)
            .addSticker(method.pngSticker, method.tgsSticker, method.webmSticker)
            .addSerializePart(MASK_POSITION, method.maskPosition, this.serializeApi)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_STICKER_TO_SET).post(it).build() }

    fun execute(method: SetStickerSetThumb): Request =
        MultipartBody.Builder()
            .addPart(USER_ID, method.userId)
            .addPart(NAME, method.name)
            .addFile(THUMB, method.thumb)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_SET_STICKER_SET_THUMB).post(it).build() }

    fun execute(method: CreateNewStickerSet): Request =
        MultipartBody.Builder()
            .addPart(USER_ID, method.userId)
            .addPart(NAME, method.name)
            .addPart(TITLE, method.title)
            .addPart(EMOJIS, method.emojis)
            .addPart(STICKER_TYPE, method.stickerType)
            .addSticker(method.pngSticker, method.tgsSticker, method.webmSticker)
            .addSerializePart(MASK_POSITION, method.maskPosition, this.serializeApi)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_CREATE_NEW_STICKER_SET).post(it).build() }

    fun execute(method: UploadStickerFile): Request =
        MultipartBody.Builder()
            .addPart(USER_ID, method.userId)
            .addFile(PNG_STICKER, method.pngSticker)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_UPLOAD_STICKER_FILE).post(it).build() }

    fun execute(method: EditMessageMedia): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addPart(MESSAGE_ID, method.messageId)
            .addPart(INLINE_MESSAGE_ID, method.inlineMessageId)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addSerializePart(MEDIA, method.media, this.serializeApi)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_EDIT_MESSAGE_MEDIA).post(it).build() }

    fun execute(method: SendAnimation): Request =
        MultipartBody.Builder()
            .addPart(CHAT_ID, method.chatId)
            .addFile(ANIMATION, method.animation)
            .addSerializePart(REPLY_MARKUP, method.replyMarkup, this.serializeApi)
            .addPart(REPLY_TO_MESSAGE_ID, method.replyToMessageId)
            .addPart(MESSAGE_THREAD_ID, method.messageThreadId)
            .addPart(DISABLE_NOTIFICATION, method.disableNotification)
            .addPart(DURATION, method.duration)
            .addPart(WIDTH, method.width)
            .addPart(HEIGHT, method.height)
            .addFile(THUMB, method.thumb, false)
            .addPart(THUMB, method.thumb?.attachName)
            .addRelatedPart(CAPTION, method.caption, PARSE_MODE, method.parseMode)
            .addPart(ALLOW_SENDING_WITHOUT_REPLY, method.allowSendingWithoutReply)
            .addPart(PROTECT_CONTENT, method.protectContent)
            .addSerializePart(CAPTION_ENTITIES, method.captionEntities, this.serializeApi)
            .addPart(HAS_SPOILER, method.hasSpoiler)
            .setType(multipartType)
            .build()
            .let { Request.Builder().url(this.baseUrl + SEND_ANIMATION).post(it).build() }

    fun <T : Serializable> executeMethod(method: BotApiMethod<T>): Request =
        Request.Builder()
            .post(this.serializeApi.serialize(method).toRequestBody(jsonType))
            .build()
}
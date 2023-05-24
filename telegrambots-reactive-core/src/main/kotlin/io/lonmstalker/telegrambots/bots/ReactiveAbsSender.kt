package io.lonmstalker.telegrambots.bots

import org.reactivestreams.Publisher
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.GetMe
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile
import org.telegram.telegrambots.meta.api.methods.updates.GetWebhookInfo
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.User
import org.telegram.telegrambots.meta.api.objects.WebhookInfo
import java.io.Serializable

interface ReactiveAbsSender {

    fun getMe(): Publisher<User> = this.sendApiMethod(GetMe(), User::class.java)

    fun getWebhookInfo(): Publisher<WebhookInfo> = this.sendApiMethod(GetWebhookInfo(), WebhookInfo::class.java)

    fun execute(method: SendDocument): Publisher<Message>

    fun execute(method: SendPhoto): Publisher<Message>

    fun execute(method: SendVideo): Publisher<Message>

    fun execute(method: SendVideoNote): Publisher<Message>

    fun execute(method: SendSticker): Publisher<Message>

    fun execute(method: SendAudio): Publisher<Message>

    fun execute(method: SendVoice): Publisher<Message>

    fun execute(method: SendMediaGroup): Publisher<List<Message>>

    fun execute(method: SetChatPhoto): Publisher<Boolean>

    fun execute(method: AddStickerToSet): Publisher<Boolean>

    fun execute(method: SetStickerSetThumb): Publisher<Boolean>

    fun execute(method: CreateNewStickerSet): Publisher<Boolean>

    fun execute(method: UploadStickerFile): Publisher<File>

    fun execute(method: EditMessageMedia): Publisher<Serializable>

    fun execute(method: SendAnimation): Publisher<Message>

    fun <T : Serializable, M : BotApiMethod<T>> sendApiMethod(method: M, clazz: Class<T>): Publisher<T>

    fun <T : Serializable, M : BotApiMethod<T>> sendApiMethodSerializable(method: M): Publisher<Serializable>
}
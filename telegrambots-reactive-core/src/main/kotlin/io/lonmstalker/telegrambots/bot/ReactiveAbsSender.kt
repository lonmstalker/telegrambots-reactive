package io.lonmstalker.telegrambots.bot

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
import reactor.core.publisher.Mono
import java.io.Serializable

interface ReactiveAbsSender {

    fun getMe(): Mono<User> = this.sendApiMethod(GetMe(), User::class.java)

    fun getWebhookInfo(): Mono<WebhookInfo> = this.sendApiMethod(GetWebhookInfo(), WebhookInfo::class.java)

    fun execute(method: SendDocument): Mono<Message>

    fun execute(method: SendPhoto): Mono<Message>

    fun execute(method: SendVideo): Mono<Message>

    fun execute(method: SendVideoNote): Mono<Message>

    fun execute(method: SendSticker): Mono<Message>

    fun execute(method: SendAudio): Mono<Message>

    fun execute(method: SendVoice): Mono<Message>

    fun execute(method: SendMediaGroup): Mono<List<Message>>

    fun execute(method: SetChatPhoto): Mono<Boolean>

    fun execute(method: AddStickerToSet): Mono<Boolean>

    fun execute(method: SetStickerSetThumb): Mono<Boolean>

    fun execute(method: CreateNewStickerSet): Mono<Boolean>

    fun execute(method: UploadStickerFile): Mono<File>

    fun execute(method: EditMessageMedia): Mono<Serializable>

    fun execute(method: SendAnimation): Mono<Message>

    fun <T : Serializable> sendApiMethod(method: BotApiMethod<T>, clazz: Class<T>): Mono<T>

    fun <T : Serializable> sendUnsafeApiMethod(method: BotApiMethod<*>, clazz: Class<T>): Mono<T>

    fun <T : Serializable> sendApiMethodSerializable(method: BotApiMethod<T>): Mono<Serializable>
}
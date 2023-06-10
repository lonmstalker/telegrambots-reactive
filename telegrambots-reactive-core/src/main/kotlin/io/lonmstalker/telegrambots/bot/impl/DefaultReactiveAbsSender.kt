package io.lonmstalker.telegrambots.bot.impl

import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.bot.ReactiveAbsSender
import io.lonmstalker.telegrambots.builder.DefaultTelegramRequestBuilder
import io.lonmstalker.telegrambots.callback.OkHttpResponseCallback
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.telegram.telegrambots.meta.api.methods.BotApiMethod
import org.telegram.telegrambots.meta.api.methods.groupadministration.SetChatPhoto
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.methods.stickers.AddStickerToSet
import org.telegram.telegrambots.meta.api.methods.stickers.CreateNewStickerSet
import org.telegram.telegrambots.meta.api.methods.stickers.SetStickerSetThumb
import org.telegram.telegrambots.meta.api.methods.stickers.UploadStickerFile
import org.telegram.telegrambots.meta.api.methods.updatingmessages.EditMessageMedia
import org.telegram.telegrambots.meta.api.objects.File
import org.telegram.telegrambots.meta.api.objects.Message
import reactor.core.publisher.Mono
import java.io.Serializable

open class DefaultReactiveAbsSender(
    private val httpClient: OkHttpClient,
    private val deserializeApi: DeserializeApi,
    botToken: String, serializeApi: SerializeApi, options: DefaultBotOptions,
) : ReactiveAbsSender {
    private val requestBuilder = DefaultTelegramRequestBuilder(botToken, serializeApi, options)

    override fun execute(method: SendDocument): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendPhoto): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendVideo): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendVideoNote): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendSticker): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendAudio): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendVoice): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendMediaGroup): Mono<List<Message>> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SetChatPhoto): Mono<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: AddStickerToSet): Mono<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SetStickerSetThumb): Mono<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: CreateNewStickerSet): Mono<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: UploadStickerFile): Mono<File> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: EditMessageMedia): Mono<Serializable> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendAnimation): Mono<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun <T : Serializable> sendApiMethodSerializable(method: BotApiMethod<T>): Mono<Serializable> =
        this.requestBuilder
            .executeMethod(method)
            .let { this.callApi(it) }

    override fun <T : Serializable> sendApiMethod(method: BotApiMethod<T>, clazz: Class<T>): Mono<T> =
        this.requestBuilder
            .executeMethod(method)
            .let { callApi(it, clazz) }

    override fun <T : Serializable> sendUnsafeApiMethod(method: BotApiMethod<*>, clazz: Class<T>): Mono<T> =
        this.requestBuilder
            .executeMethod(method)
            .let { callApi(it, clazz) }

    private fun <T> callApi(request: Request, clazz: Class<T>): Mono<T> =
        Mono.create {
            this.httpClient
                .newCall(request)
                .enqueue(OkHttpResponseCallback(clazz, it, this.deserializeApi))
        }

    private inline fun <reified T> callApi(request: Request): Mono<T> = callApi(request, T::class.java)

}
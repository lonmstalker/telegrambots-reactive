package io.lonmstalker.telegrambots.bot.impl

import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.bot.ReactiveAbsSender
import io.lonmstalker.telegrambots.builder.DefaultTelegramRequestBuilder
import io.lonmstalker.telegrambots.callback.OkHttpResponseCallback
import io.lonmstalker.telegrambots.serde.DeserializeApi
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.OkHttpClient
import okhttp3.Request
import org.reactivestreams.Publisher
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

    override fun execute(method: SendDocument): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendPhoto): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendVideo): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendVideoNote): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendSticker): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendAudio): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendVoice): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendMediaGroup): Publisher<List<Message>> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SetChatPhoto): Publisher<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: AddStickerToSet): Publisher<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SetStickerSetThumb): Publisher<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: CreateNewStickerSet): Publisher<Boolean> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: UploadStickerFile): Publisher<File> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: EditMessageMedia): Publisher<Serializable> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun execute(method: SendAnimation): Publisher<Message> =
        this.requestBuilder
            .execute(method)
            .let { this.callApi(it) }

    override fun <T : Serializable, M : BotApiMethod<T>> sendApiMethodSerializable(method: M): Publisher<Serializable> =
        this.requestBuilder
            .executeMethod(method)
            .let { this.callApi(it) }

    override fun <T : Serializable, M : BotApiMethod<T>> sendApiMethod(method: M, clazz: Class<T>): Publisher<T> =
        this.requestBuilder
            .executeMethod(method)
            .let { request ->
                Mono.create {
                    this.httpClient
                        .newCall(request)
                        .enqueue(OkHttpResponseCallback(clazz, it, this.deserializeApi))
                }
            }

    private inline fun <reified T> callApi(request: Request): Publisher<T> =
        Mono.create {
            this.httpClient
                .newCall(request)
                .enqueue(OkHttpResponseCallback(T::class.java, it, this.deserializeApi))
        }

}
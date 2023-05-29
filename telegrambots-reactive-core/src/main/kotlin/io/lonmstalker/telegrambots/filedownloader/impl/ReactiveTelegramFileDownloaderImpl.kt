package io.lonmstalker.telegrambots.filedownloader.impl

import io.lonmstalker.telegrambots.constants.TelegramBotsConstants.DEFAULT_CHUNK_SIZE
import io.lonmstalker.telegrambots.constants.TelegramUrlConstants.TELEGRAM_FILE_URL
import io.lonmstalker.telegrambots.filedownloader.ReactiveTelegramFileDownloader
import io.lonmstalker.telegrambots.callback.OkFluxFileDownloadCallback
import io.lonmstalker.telegrambots.callback.OkMonoFileDownloadCallback
import okhttp3.OkHttpClient
import okhttp3.Request
import org.apache.commons.io.FileUtils
import org.reactivestreams.Publisher
import org.telegram.telegrambots.meta.api.objects.File
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.io.InputStream
import java.util.*

class ReactiveTelegramFileDownloaderImpl @JvmOverloads constructor(
    private val httpClient: OkHttpClient,
    private val chunkSize: Int = DEFAULT_CHUNK_SIZE,
    private val apiUrlPattern: String = TELEGRAM_FILE_URL
) : ReactiveTelegramFileDownloader {
    private val tmpSuffix = ".tmp"

    override fun downloadFile(botToken: String, file: File): Publisher<java.io.File> =
        Request.Builder()
            .url(this.getUri(botToken, file.filePath))
            .build()
            .let { this.callApi(it) }
            .map { this.getTempFile(file.fileId).apply { FileUtils.copyInputStreamToFile(it, this) } }

    override fun downloadFile(botToken: String, filePath: String): Publisher<java.io.File> =
        Request.Builder()
            .url(this.getUri(botToken, filePath))
            .build()
            .let { this.callApi(it) }
            .map { this.getTempFile().apply { FileUtils.copyInputStreamToFile(it, this) } }

    override fun downloadFile(botToken: String, file: File, output: java.io.File): Publisher<Void> =
        Request.Builder()
            .url(this.getUri(botToken, file.filePath))
            .build()
            .let { this.callApi(it) }
            .flatMap { Mono.fromRunnable<Unit> { FileUtils.copyInputStreamToFile(it, output) } }
            .then()

    override fun downloadFileAsStream(botToken: String, file: File): Publisher<ByteArray> =
        Request.Builder()
            .url(this.getUri(botToken, file.filePath))
            .build()
            .let { this.callApiStream(it) }

    override fun downloadFileAsStream(botToken: String, filePath: String): Publisher<ByteArray> =
        Request.Builder()
            .url(this.getUri(botToken, filePath))
            .build()
            .let { this.callApiStream(it) }

    private fun callApi(request: Request): Mono<InputStream> =
        Mono.create {
            this.httpClient
                .newCall(request)
                .enqueue(OkMonoFileDownloadCallback(it))
        }

    private fun callApiStream(request: Request): Flux<ByteArray> =
        Flux.create {
            this.httpClient
                .newCall(request)
                .enqueue(OkFluxFileDownloadCallback(this.chunkSize, it))
        }

    private fun getTempFile(fileName: String = System.currentTimeMillis().toString()) =
        java.io.File.createTempFile(fileName, tmpSuffix)

    private fun getUri(botToken: String, filePath: String) =
        this.apiUrlPattern.format(Locale.getDefault(), botToken, filePath)
}
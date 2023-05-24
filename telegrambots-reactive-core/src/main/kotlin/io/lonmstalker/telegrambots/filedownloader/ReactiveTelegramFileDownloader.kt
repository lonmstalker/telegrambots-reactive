package io.lonmstalker.telegrambots.filedownloader

import org.reactivestreams.Publisher
import org.telegram.telegrambots.meta.api.objects.File

interface ReactiveTelegramFileDownloader {
    fun downloadFile(botToken: String, file: File): Publisher<java.io.File>
    fun downloadFile(botToken: String, filePath: String): Publisher<java.io.File>
    fun downloadFileAsStream(botToken: String, file: File): Publisher<ByteArray>
    fun downloadFile(botToken: String, file: File, output: java.io.File): Publisher<Void>
    fun downloadFileAsStream(botToken: String, filePath: String): Publisher<ByteArray>
}
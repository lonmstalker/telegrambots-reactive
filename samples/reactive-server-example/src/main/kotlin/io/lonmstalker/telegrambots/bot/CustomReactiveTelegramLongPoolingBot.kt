package io.lonmstalker.telegrambots.bot

import com.fasterxml.jackson.databind.ObjectMapper
import io.lonmstalker.telegrambots.bots.impl.ReactiveTelegramLongPoolingBot
import io.lonmstalker.telegrambots.constants.Constants.BOT_DEFAULT_MESSAGE
import io.lonmstalker.telegrambots.constants.Constants.IMAGE_CAPTION
import io.lonmstalker.telegrambots.constants.Constants.IMAGE_FILENAME
import io.lonmstalker.telegrambots.constants.Constants.IMAGE_PATH
import io.lonmstalker.telegrambots.constants.Constants.MY_BOT_USERNAME
import io.lonmstalker.telegrambots.constants.Constants.START_MESSAGE
import io.lonmstalker.telegrambots.properties.AppProperties
import okhttp3.OkHttpClient
import org.apache.commons.io.FileUtils
import org.apache.commons.io.FileUtils.openInputStream
import org.reactivestreams.Publisher
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.methods.send.SendPhoto
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Update
import reactor.core.publisher.Flux

class CustomReactiveTelegramLongPoolingBot(
    httpClient: OkHttpClient,
    appProperties: AppProperties,
    objectMapper: ObjectMapper
) : ReactiveTelegramLongPoolingBot(httpClient, { appProperties.bot.token }, objectMapper) {

    override fun onUpdateReceived(update: Flux<Update>): Publisher<Void> =
        update
            .flatMap {
                when(it.message.text) {
                    START_MESSAGE -> this.sendApiMethodSerializable(createResponse(it))
                    else -> this.execute(this.createImageResponse(it))
                }
            }
            .then()

    private fun createResponse(update: Update): SendMessage =
        SendMessage()
            .apply {
                this.chatId = update.message.chatId.toString()
                this.text = BOT_DEFAULT_MESSAGE
                this.enableMarkdownV2(true)
            }

    private fun createImageResponse(update: Update): SendPhoto =
        SendPhoto()
            .apply {
                this.chatId = update.message.chatId.toString()
                this.caption = IMAGE_CAPTION
                this.disableNotification = true
                this.hasSpoiler = true
                this.photo = InputFile(openInputStream(FileUtils.getFile(IMAGE_PATH)), IMAGE_FILENAME)
            }

    override fun getBotUsername(): String = MY_BOT_USERNAME

}
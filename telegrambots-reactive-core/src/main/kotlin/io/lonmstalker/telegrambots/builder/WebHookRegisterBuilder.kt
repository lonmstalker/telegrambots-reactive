package io.lonmstalker.telegrambots.builder

import io.lonmstalker.telegrambots.bot.DefaultBotOptions
import io.lonmstalker.telegrambots.constants.MediaTypeConstants
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.ALLOWED_UPDATES
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.CALLBACK
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.CERTIFICATE
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.DROP_PENDING_UPDATES
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.IP_ADDRESS
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.MAX_CONNECTIONS
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.SECRET_TOKEN
import io.lonmstalker.telegrambots.constants.TelegramApiConstants.URL
import io.lonmstalker.telegrambots.constants.TelegramBotsConstants.SLASH
import io.lonmstalker.telegrambots.constants.TelegramSendConstants.SEND_WEBHOOK
import io.lonmstalker.telegrambots.extension.internal.addFile
import io.lonmstalker.telegrambots.extension.internal.addPart
import io.lonmstalker.telegrambots.extension.internal.addSerializePart
import io.lonmstalker.telegrambots.serde.SerializeApi
import okhttp3.MultipartBody
import okhttp3.Request
import org.apache.commons.lang3.StringUtils.EMPTY
import org.telegram.telegrambots.meta.api.methods.updates.SetWebhook

class WebHookRegisterBuilder(
    private val serializeApi: SerializeApi,
) {

    fun registerWebHook(botPath: String?, webHook: SetWebhook, options: DefaultBotOptions): Request {
        webHook.validate()
        return MultipartBody.Builder()
            .addPart(URL, this.getBotUrl(webHook.url, botPath))
            .addPart(MAX_CONNECTIONS, webHook.maxConnections)
            .addSerializePart(ALLOWED_UPDATES, webHook.allowedUpdates, this.serializeApi)
            .addPart(IP_ADDRESS, webHook.ipAddress)
            .addPart(DROP_PENDING_UPDATES, webHook.dropPendingUpdates)
            .addPart(SECRET_TOKEN, webHook.secretToken)
            .addFile(CERTIFICATE, webHook.certificate)
            .setType(MediaTypeConstants.multipartType)
            .build()
            .let { Request.Builder().url(options.baseUrl + SEND_WEBHOOK).post(it).build() }
    }

    private fun getBotUrl(externalUrl: String, botPath: String?): String =
        StringBuilder(externalUrl)
            .append(
                if (!externalUrl.endsWith(SLASH)) {
                    SLASH
                } else {
                    EMPTY
                }
            )
            .append(CALLBACK)
            .append(
                if (!botPath.isNullOrEmpty()) {
                    if (botPath.startsWith(SLASH)) {
                        SLASH + botPath
                    } else {
                        botPath
                    }
                } else {
                    EMPTY
                }
            )
            .toString()
}
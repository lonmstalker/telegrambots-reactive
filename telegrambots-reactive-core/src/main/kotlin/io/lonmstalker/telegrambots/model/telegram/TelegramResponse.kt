package io.lonmstalker.telegrambots.model.telegram

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty

@JsonIgnoreProperties(ignoreUnknown = true)
data class TelegramResponse<T>(
    @field:JsonProperty("ok") val ok: Boolean,
    @field:JsonProperty("result") val result: T
)

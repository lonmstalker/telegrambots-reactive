package io.lonmstalker.telegrambots.model.telegram

import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonProperty
import java.io.Serializable

@JsonIgnoreProperties(ignoreUnknown = true)
data class DeleteWebhookResponse(@field:JsonProperty("ok") val ok: Boolean) : Serializable

package io.lonmstalker.telegrambots.model

import org.telegram.telegrambots.meta.api.objects.Update

data class UpdateData(
    val botId: Int,
    val update: Update
)

package io.lonmstalker.telegrambots.config

import io.lonmstalker.telegrambots.model.UpdateData
import kotlinx.coroutines.flow.MutableSharedFlow
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class FlowConfiguration {

    @Bean
    fun sinkFlow() = MutableSharedFlow<UpdateData>()
}
package io.lonmstalker.telegrambots

import io.lonmstalker.telegrambots.properties.AppProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableConfigurationProperties(AppProperties::class)
class ReactiveServerExampleApplication

fun main(args: Array<String>) {
    runApplication<ReactiveServerExampleApplication>(*args)
}

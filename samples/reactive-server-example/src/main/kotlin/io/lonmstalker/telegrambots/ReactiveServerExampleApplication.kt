package io.lonmstalker.telegrambots

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ReactiveServerExampleApplication

fun main(args: Array<String>) {
    runApplication<ReactiveServerExampleApplication>(*args)
}

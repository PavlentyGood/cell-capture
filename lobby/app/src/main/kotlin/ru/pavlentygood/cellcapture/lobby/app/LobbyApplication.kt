package ru.pavlentygood.cellcapture.lobby.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class LobbyApplication

fun main(args: Array<String>) {
    runApplication<LobbyApplication>(*args)
}

package ru.pavlentygood.cellcapture.app

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class CellCaptureApplication

fun main(args: Array<String>) {
    runApplication<CellCaptureApplication>(*args)
}

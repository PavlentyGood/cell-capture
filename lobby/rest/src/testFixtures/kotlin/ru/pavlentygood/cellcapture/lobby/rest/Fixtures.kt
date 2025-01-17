package ru.pavlentygood.cellcapture.lobby.rest

import com.fasterxml.jackson.module.kotlin.jacksonObjectMapper

val mapper = jacksonObjectMapper()

fun String.with(name: String, value: Any) =
    replace("{$name}", value.toString())

package ru.pavlentygood.cellcapture.lobby.persistence.dto

interface OutboxReadDto {

    val id: Long
    val eventType: EventTypeDto
    val body: String
}

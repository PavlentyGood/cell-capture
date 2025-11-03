package ru.pavlentygood.cellcapture.lobby.persistence.dto

import org.postgresql.util.PGobject

class OutboxReadDto(

    val id: Long,

    val eventType: EventTypeDto,

    val body: PGobject
)

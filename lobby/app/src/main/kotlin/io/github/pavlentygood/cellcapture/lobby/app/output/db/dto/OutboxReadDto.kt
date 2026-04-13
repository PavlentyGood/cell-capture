package io.github.pavlentygood.cellcapture.lobby.app.output.db.dto

import org.postgresql.util.PGobject

class OutboxReadDto(

    val id: Long,

    val eventType: EventTypeDto,

    val body: PGobject
)

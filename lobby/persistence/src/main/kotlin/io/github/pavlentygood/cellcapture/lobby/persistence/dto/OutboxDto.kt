package io.github.pavlentygood.cellcapture.lobby.persistence.dto

import org.postgresql.util.PGobject
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table

@Table(name = "outbox")
class OutboxDto(

    @Id
    var id: Long = 0,

    val aggregateId: String,

    val status: String,

    val eventType: EventTypeDto,

    val body: PGobject
)

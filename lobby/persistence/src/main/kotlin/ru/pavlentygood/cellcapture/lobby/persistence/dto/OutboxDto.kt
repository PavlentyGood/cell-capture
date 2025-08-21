package ru.pavlentygood.cellcapture.lobby.persistence.dto

import jakarta.persistence.*
import org.hibernate.annotations.ColumnTransformer
import java.time.LocalDateTime

@Entity
@Table(name = "outbox")
class OutboxDto(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,

    val aggregateId: String,

    val status: String,

    @Enumerated(EnumType.STRING)
    val eventType: EventTypeDto,

    @ColumnTransformer(write = "?::json")
    val body: String,

    val created: LocalDateTime,

    val updated: LocalDateTime
)

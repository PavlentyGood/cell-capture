package ru.pavlentygood.cellcapture.lobby.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.jdbc.core.JdbcTemplate
import org.springframework.transaction.annotation.Transactional
import ru.pavlentygood.cellcapture.kernel.common.VersionConflictException
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PartyEvent
import ru.pavlentygood.cellcapture.lobby.domain.PartyStartedEvent
import ru.pavlentygood.cellcapture.lobby.persistence.dto.*
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty
import java.time.LocalDateTime

@Transactional
class SavePartyToDatabase(
    private val partyRepository: PartyRepository,
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper,
    private val jdbcTemplate: JdbcTemplate
) : SaveParty {

    override fun invoke(party: Party) {
        val version = getPartyVersion(party.id)
        if (version != null && party.version.previous().value != version) {
            throw VersionConflictException()
        }

        val partyDto = party.toDto()
        partyRepository.save(partyDto)

        party.popEvents()
            .filterIsInstance<PartyStartedEvent>()
            .map { it.toOutboxDto(party.id, objectMapper) }
            .let { outboxRepository.saveAll(it) }
    }

    private fun getPartyVersion(partyId: PartyId) =
        jdbcTemplate.queryForList(
            "select version from parties where id = ? for update",
            Long::class.java,
            partyId.toUUID()
        ).singleOrNull()
}

fun Party.toDto(): PartyDto {
    val party = PartyDto(
        id = id.toUUID(),
        version = version.value,
        started = started,
        ownerId = ownerId.toInt(),
        playerLimit = playerLimit.value,
        players = listOf()
    )
    party.players = getPlayers().map { it.toDto(party) }
    return party
}

fun Player.toDto(party: PartyDto) =
    PlayerDto(
        id = id.toInt(),
        name = name.toStringValue(),
        party = party
    )

fun PartyEvent.toOutboxDto(aggregateId: PartyId, om: ObjectMapper) =
    OutboxDto(
        aggregateId = aggregateId.toUUID().toString(),
        status = "PENDING",
        eventType = this.getType(),
        body = om.writeValueAsString(this.toDto()),
        created = LocalDateTime.now(),
        updated = LocalDateTime.now()
    )

fun PartyEvent.getType() =
    when (this) {
        is PartyStartedEvent -> EventTypeDto.PARTY_STARTED
        else -> error("Illegal event type for outbox")
    }

fun PartyEvent.toDto() =
    when (this) {
        is PartyStartedEvent -> this.toDto()
        else -> error("Illegal event for outbox")
    }

fun PartyStartedEvent.toDto() =
    PartyStartedEventDto(
        partyId = partyId.toUUID(),
        ownerId = ownerId.toInt(),
        players = players.map {
            PartyStartedEventDto.Player(
                id = it.id.toInt(),
                name = it.name.toStringValue()
            )
        }
    )

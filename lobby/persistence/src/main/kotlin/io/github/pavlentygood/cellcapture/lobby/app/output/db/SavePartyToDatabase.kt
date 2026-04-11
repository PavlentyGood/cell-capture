package io.github.pavlentygood.cellcapture.lobby.app.output.db

import com.fasterxml.jackson.databind.ObjectMapper
import io.github.pavlentygood.cellcapture.kernel.common.VersionConflictException
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.EventTypeDto
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.OutboxDto
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.PartyDto
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.PartyStartedEventDto
import io.github.pavlentygood.cellcapture.lobby.app.output.db.dto.PlayerDto
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.domain.PartyEvent
import io.github.pavlentygood.cellcapture.lobby.domain.PartyStartedEvent
import io.github.pavlentygood.cellcapture.lobby.domain.Player
import io.github.pavlentygood.cellcapture.lobby.usecase.port.SaveParty
import org.postgresql.util.PGobject
import org.springframework.transaction.annotation.Transactional

@Transactional
class SavePartyToDatabase(
    private val partyRepository: PartyRepository,
    private val outboxRepository: OutboxRepository,
    private val objectMapper: ObjectMapper
) : SaveParty {

    override fun invoke(party: Party) {
        val version = partyRepository.getVersion(party.id.toUUID())
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
}

fun Party.toDto() =
    PartyDto(
        partyId = id.toUUID(),
        version = version.value,
        started = started,
        ownerId = ownerId.toInt(),
        playerLimit = playerLimit.value,
        players = getPlayers().map { it.toDto() }
    )

fun Player.toDto() =
    PlayerDto(
        id = id.toInt(),
        name = name.toStringValue()
    )

fun PartyEvent.toOutboxDto(aggregateId: PartyId, om: ObjectMapper) =
    OutboxDto(
        aggregateId = aggregateId.toUUID().toString(),
        status = "PENDING",
        eventType = this.getType(),
        body = PGobject().apply {
            type = "json"
            value = om.writeValueAsString(this@toOutboxDto.toDto())
        }
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

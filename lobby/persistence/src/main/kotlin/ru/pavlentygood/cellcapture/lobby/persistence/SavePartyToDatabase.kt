package ru.pavlentygood.cellcapture.lobby.persistence

import com.fasterxml.jackson.databind.ObjectMapper
import org.springframework.transaction.annotation.Transactional
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
    private val objectMapper: ObjectMapper
) : SaveParty {

    override fun invoke(party: Party) {
        val partyDto = party.toDto()
        partyRepository.save(partyDto)
        party.getEvents()
            .filterIsInstance<PartyStartedEvent>()
            .map { it.toOutboxDto(party.id, objectMapper) }
            .let { outboxRepository.saveAll(it) }
    }
}

fun Party.toDto(): PartyDto {
    val party = PartyDto(
        id = id.toUUID(),
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
    }

fun PartyEvent.toDto() =
    when (this) {
        is PartyStartedEvent -> this.toDto()
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

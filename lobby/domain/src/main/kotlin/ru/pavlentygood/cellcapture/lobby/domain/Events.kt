package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainEvent

sealed interface PartyEvent : DomainEvent

data class PartyCreatedEvent(
    val partyId: PartyId
) : PartyEvent

data class PlayerJoinedEvent(
    val partyId: PartyId,
    val player: Player
) : PartyEvent

data class PartyStartedEvent(
    val partyId: PartyId,
    val ownerId: PlayerId,
    val players: List<Player>
) : PartyEvent

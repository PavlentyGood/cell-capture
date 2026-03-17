package io.github.pavlentygood.cellcapture.lobby.domain

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.base.DomainEvent

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

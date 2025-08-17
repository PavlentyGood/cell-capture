package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

data class PartyStartedEvent(
    val partyId: PartyId,
    val ownerId: PlayerId,
    val players: List<Player>
) : PartyEvent

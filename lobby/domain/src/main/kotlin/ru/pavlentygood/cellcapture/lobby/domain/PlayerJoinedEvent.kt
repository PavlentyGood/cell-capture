package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player

data class PlayerJoinedEvent(
    val partyId: PartyId,
    val player: Player
) : PartyEvent

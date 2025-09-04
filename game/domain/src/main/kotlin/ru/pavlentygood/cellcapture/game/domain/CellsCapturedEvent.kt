package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

data class CellsCapturedEvent(
    val partyId: PartyId,
    val playerId: PlayerId,
    val capturedArea: Area
) : PartyEvent

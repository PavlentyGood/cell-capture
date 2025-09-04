package ru.pavlentygood.cellcapture.game.domain.event

import ru.pavlentygood.cellcapture.kernel.domain.PartyId

data class PartyCreatedEvent(
    val partyId: PartyId
) : PartyEvent

package ru.pavlentygood.cellcapture.lobby.domain.event

import ru.pavlentygood.cellcapture.kernel.domain.PartyId

data class PartyCreatedEvent(
    val partyId: PartyId
) : PartyEvent

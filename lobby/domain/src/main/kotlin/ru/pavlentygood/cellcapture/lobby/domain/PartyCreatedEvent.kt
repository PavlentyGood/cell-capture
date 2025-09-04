package ru.pavlentygood.cellcapture.lobby.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId

data class PartyCreatedEvent(
    val partyId: PartyId
) : PartyEvent

package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId

data class PartyCreatedEvent(
    val partyId: PartyId
) : PartyEvent

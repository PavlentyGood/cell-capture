package ru.pavlentygood.cellcapture.game.domain

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainEvent

sealed interface PartyEvent : DomainEvent

data class PartyCreatedEvent(
    val partyId: PartyId
) : PartyEvent

data class DicesRolledEvent(
    val partyId: PartyId,
    val playerId: PlayerId,
    val dices: RolledDices
) : PartyEvent

data class CellsCapturedEvent(
    val partyId: PartyId,
    val playerId: PlayerId,
    val capturedArea: Area
) : PartyEvent

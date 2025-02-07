package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class CompletedParty(
    id: PartyId
) : AbstractParty(id) {

    override fun roll(playerId: PlayerId) =
        PartyAlreadyCompleted.left()

    override fun capture(playerId: PlayerId, area: Area) =
        PartyAlreadyCompleted.left()
}

data object PartyAlreadyCompleted : Party.RollDicesError, Party.CaptureCellsError

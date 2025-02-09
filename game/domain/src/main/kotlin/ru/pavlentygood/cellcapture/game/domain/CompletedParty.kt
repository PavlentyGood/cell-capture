package ru.pavlentygood.cellcapture.game.domain

import arrow.core.left
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class CompletedParty(
    id: PartyId,
    override val dices: Dices,
    override val cells: Array<Array<PlayerId>>,
    override val ownerId: PlayerId,
    override val currentPlayerId: PlayerId,
    override val players: List<Player>
) : Party(id) {

    override fun roll(playerId: PlayerId) =
        PartyCompleted.left()

    override fun capture(playerId: PlayerId, area: Area) =
        PartyCompleted.left()
}

data object PartyCompleted : RollDicesError, CaptureCellsError

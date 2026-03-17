package io.github.pavlentygood.cellcapture.game.domain

import arrow.core.left
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.base.Version

class CompletedParty(
    id: PartyId,
    version: Version,
    override val dices: Dices,
    override val cells: Array<Array<Cell>>,
    override val ownerId: PlayerId,
    override val currentPlayerId: PlayerId,
    override val players: List<Player>
) : Party(id, version) {

    override val completed = true

    override fun roll(playerId: PlayerId) =
        PartyCompleted.left()

    override fun capture(playerId: PlayerId, area: Area) =
        PartyCompleted.left()
}

data object PartyCompleted : RollDicesError, CaptureCellsError

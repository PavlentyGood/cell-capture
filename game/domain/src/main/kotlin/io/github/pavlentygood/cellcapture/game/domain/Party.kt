package io.github.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot
import io.github.pavlentygood.cellcapture.kernel.domain.base.Version

sealed class Party(
    id: PartyId,
    version: Version
) : AggregateRoot<PartyId, PartyEvent>(id, version) {

    abstract val completed: Boolean
    abstract val ownerId: PlayerId
    abstract val currentPlayerId: PlayerId
    abstract val dices: Dices
    abstract val players: List<Player>
    abstract val cells: Array<Array<Cell>>

    abstract fun roll(playerId: PlayerId): Either<RollDicesError, RolledDices>
    abstract fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit>
}

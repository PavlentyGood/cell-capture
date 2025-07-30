package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot

sealed class Party(
    id: PartyId
) : AggregateRoot<PartyId>(id) {

    abstract val completed: Boolean
    abstract val ownerId: PlayerId
    abstract val currentPlayerId: PlayerId
    abstract val dices: Dices
    abstract val players: List<Player>
    abstract val cells: Array<Array<Cell>>

    abstract fun roll(playerId: PlayerId): Either<RollDicesError, RolledDices>
    abstract fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit>
}

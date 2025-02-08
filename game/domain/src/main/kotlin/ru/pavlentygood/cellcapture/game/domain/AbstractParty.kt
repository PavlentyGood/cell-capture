package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import ru.pavlentygood.cellcapture.game.domain.Party.CaptureCellsError
import ru.pavlentygood.cellcapture.game.domain.Party.RollDicesError
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.AggregateRoot

sealed class AbstractParty(
    id: PartyId
) : AggregateRoot<PartyId>(id) {

    abstract val ownerId: PlayerId

    abstract val currentPlayerId: PlayerId

    abstract val dices: Dices

    abstract fun getPlayers(): List<Player>

    abstract fun getCells(): Array<Array<PlayerId>>

    abstract fun roll(playerId: PlayerId): Either<RollDicesError, RolledDices>

    abstract fun capture(playerId: PlayerId, area: Area): Either<CaptureCellsError, Unit>
}

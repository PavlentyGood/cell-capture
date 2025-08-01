package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.MAX_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.MIN_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.base.DomainError

data class PlayerList internal constructor(
    val ownerId: PlayerId,
    val players: List<Player>
) {
    val playerIds: List<PlayerId> = players.ids

    companion object {

        fun from(
            ownerId: PlayerId,
            players: List<Player>
        ): Either<PartyInfoError, PlayerList> {
            return when {
                players.ids.none { it == ownerId } ->
                    IllegalOwnerId.left()
                players.size !in MIN_PLAYER_COUNT..MAX_PLAYER_COUNT ->
                    IllegalPlayerCount.left()
                players.size > players.map { it.id }.distinct().size ->
                    DuplicatePlayerIds.left()
                else -> PlayerList(
                    ownerId = ownerId,
                    players = players
                ).right()
            }
        }

        private val Collection<Player>.ids get() =
            this.map { it.id }
    }
}

sealed interface PartyInfoError : DomainError
data object IllegalOwnerId : PartyInfoError
data object IllegalPlayerCount : PartyInfoError
data object DuplicatePlayerIds : PartyInfoError

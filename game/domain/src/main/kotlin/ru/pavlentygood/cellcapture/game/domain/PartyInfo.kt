package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right

const val MIN_PLAYER_COUNT = 2
const val MAX_PLAYER_COUNT = 16

data class PartyInfo internal constructor(
    val id: PartyId,
    val ownerId: PlayerId,
    val players: List<Player>
) {
    val playerIds: List<PlayerId> = players.ids

    companion object {

        fun from(
            id: PartyId,
            ownerId: PlayerId,
            players: List<Player>
        ): Either<PartyInfoError, PartyInfo> {
            return when {
                players.ids.none { it == ownerId } ->
                    UnmatchedOwnerId.left()
                players.size !in MIN_PLAYER_COUNT..MAX_PLAYER_COUNT ->
                    IllegalPlayerCount.left()
                players.size > players.map { it.id }.distinct().size ->
                    DuplicatePlayerIds.left()
                else -> PartyInfo(
                    id = id,
                    ownerId = ownerId,
                    players = players
                ).right()
            }
        }

        private val Collection<Player>.ids get() =
            this.map { it.id }
    }
}

sealed interface PartyInfoError
data object UnmatchedOwnerId : PartyInfoError
data object IllegalPlayerCount : PartyInfoError
data object DuplicatePlayerIds : PartyInfoError

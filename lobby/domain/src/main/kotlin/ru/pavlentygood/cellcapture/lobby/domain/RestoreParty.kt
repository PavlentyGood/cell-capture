package ru.pavlentygood.cellcapture.lobby.domain

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.MIN_PLAYER_COUNT
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class RestoreParty {

    operator fun invoke(
        id: PartyId,
        started: Boolean,
        playerLimit: PlayerLimit,
        players: List<Player>,
        ownerId: PlayerId
    ): Either<RestorePartyError, Party> =
        when {
            !started && players.isEmpty() -> TooFewPlayers.left()
            started && players.size < MIN_PLAYER_COUNT -> TooFewPlayers.left()
            playerLimit.isExceeded(players.size) -> TooManyPlayers.left()
            players.none { it.id == ownerId } -> IllegalOwnerId.left()
            else -> Party(
                id = id,
                started = started,
                playerLimit = playerLimit,
                players = players,
                ownerId = ownerId
            ).right()
        }
}

sealed interface RestorePartyError
data object TooFewPlayers : RestorePartyError
data object TooManyPlayers : RestorePartyError
data object IllegalOwnerId : RestorePartyError

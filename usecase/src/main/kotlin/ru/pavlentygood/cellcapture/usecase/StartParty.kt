package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PlayerId

class StartParty(
    private val getPartyByPlayer: GetPartyByPlayer
) {
    operator fun invoke(playerId: PlayerId): Either<Error, Unit> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.start(playerId)
                    .mapLeft { it.toUseCaseError() }
            } ?: PlayerNotFound.left()

    private fun Party.Start.toUseCaseError() =
        when (this) {
            Party.PlayerNotOwner -> PlayerNotOwner
            Party.TooFewPlayers -> TooFewPlayers
            Party.AlreadyStarted -> AlreadyStarted
            Party.AlreadyCompleted -> AlreadyCompleted
        }

    sealed class Error
    data object PlayerNotFound : Error()
    data object PlayerNotOwner : Error()
    data object TooFewPlayers : Error()
    data object AlreadyStarted : Error()
    data object AlreadyCompleted : Error()
}

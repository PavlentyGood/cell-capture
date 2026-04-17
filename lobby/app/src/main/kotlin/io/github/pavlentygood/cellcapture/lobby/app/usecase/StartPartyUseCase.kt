package io.github.pavlentygood.cellcapture.lobby.app.usecase

import arrow.core.Either
import arrow.core.left
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.lobby.domain.Party

class StartPartyUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty
) {
    operator fun invoke(playerId: PlayerId): Either<Error, Unit> =
        getPartyByPlayer(playerId)
            ?.let { party: Party ->
                party.start(playerId)
                    .map { saveParty(party) }
                    .mapLeft { it.toUseCaseError() }
            } ?: PlayerNotFound.left()

    private fun Party.StartPartyError.toUseCaseError() =
        when (this) {
            Party.PlayerNotOwner -> PlayerNotOwner
            Party.TooFewPlayers -> TooFewPlayers
            Party.AlreadyStarted -> AlreadyStarted
        }

    sealed class Error
    data object PlayerNotFound : Error()
    data object PlayerNotOwner : Error()
    data object TooFewPlayers : Error()
    data object AlreadyStarted : Error()
}

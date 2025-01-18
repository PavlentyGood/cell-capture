package ru.pavlentygood.cellcapture.lobby.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

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

    private fun Party.Start.toUseCaseError() =
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

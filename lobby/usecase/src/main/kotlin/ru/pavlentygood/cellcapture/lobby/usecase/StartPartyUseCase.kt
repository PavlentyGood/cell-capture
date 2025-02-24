package ru.pavlentygood.cellcapture.lobby.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.lobby.usecase.port.PublishPartyStartedEvent
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

class StartPartyUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty,
    private val publishPartyStartedEvent: PublishPartyStartedEvent
) {
    operator fun invoke(playerId: PlayerId): Either<Error, Unit> =
        getPartyByPlayer(playerId)
            ?.let { party: Party ->
                party.start(playerId)
                    .map {
                        saveParty(party)
                        publishPartyStartedEvent(party)
                    }
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

package ru.pavlentygood.cellcapture.lobby.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.lobby.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

class JoinPlayerUseCase(
    private val getParty: GetParty,
    private val saveParty: SaveParty,
    private val generatePlayerId: GeneratePlayerId
) {
    operator fun invoke(partyId: PartyId, name: PlayerName): Either<JoinPlayerUseCaseError, PlayerId> =
        getParty(partyId)
            ?.let { party ->
                party.joinPlayer(name, generatePlayerId)
                    .mapLeft { it.toUseCaseError() }
                    .map { playerId ->
                        saveParty(party)
                        playerId
                    }
            } ?: PartyNotFoundUseCaseError.left()

    private fun Party.JoinPlayerError.toUseCaseError() =
        when (this) {
            Party.AlreadyStarted -> AlreadyStartedUseCaseError
            Party.PlayerCountLimit -> PlayerCountLimitUseCaseError
        }
}

sealed interface JoinPlayerUseCaseError
data object PartyNotFoundUseCaseError : JoinPlayerUseCaseError
data object AlreadyStartedUseCaseError : JoinPlayerUseCaseError
data object PlayerCountLimitUseCaseError : JoinPlayerUseCaseError

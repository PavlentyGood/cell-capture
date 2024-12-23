package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.domain.PlayerName
import ru.pavlentygood.cellcapture.usecase.port.GetParty
import ru.pavlentygood.cellcapture.usecase.port.SaveParty

class JoinPlayerUseCase(
    private val getParty: GetParty,
    private val saveParty: SaveParty,
    private val generatePlayerId: GeneratePlayerId
) {
    operator fun invoke(partyId: PartyId, name: PlayerName): Either<JoinPlayerError, PlayerId> =
        getParty(partyId)
            ?.let { party ->
                party.joinPlayer(name, generatePlayerId)
                    .mapLeft { PlayerCountLimitUseCaseError }
                    .map { playerId ->
                        saveParty(party)
                        playerId
                    }
            } ?: PartyNotFoundUseCaseError.left()
}

sealed class JoinPlayerError
data object PartyNotFoundUseCaseError : JoinPlayerError()
data object PlayerCountLimitUseCaseError : JoinPlayerError()

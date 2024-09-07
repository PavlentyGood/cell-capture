package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import arrow.core.flatMap
import ru.pavlentygood.cellcapture.domain.GeneratePlayerId
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.domain.PlayerName

class JoinPlayer(
    private val getParty: GetParty,
    private val saveParty: SaveParty,
    private val generatePlayerId: GeneratePlayerId
) {
    operator fun invoke(partyId: PartyId, name: PlayerName): Either<JoinPlayerError, PlayerId> =
        getParty(partyId)
            .flatMap { party ->
                party.joinPlayer(name, generatePlayerId)
                    .mapLeft { PlayerCountLimitExceededUseCaseError }
                    .map { playerId ->
                        saveParty(party)
                        playerId
                    }
            }
}

sealed class JoinPlayerError
data object PartyNotFoundUseCaseError : JoinPlayerError()
data object PlayerCountLimitExceededUseCaseError : JoinPlayerError()

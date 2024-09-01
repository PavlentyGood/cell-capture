package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import arrow.core.right
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.domain.PlayerName

class JoinPlayer {
    operator fun invoke(partyId: PartyId, name: PlayerName): Either<JoinPlayerError, PlayerId> =
        PlayerId(1).right()
}

sealed class JoinPlayerError
data object PartyNotFound : JoinPlayerError()
data object PlayerCountLimitExceeded : JoinPlayerError()

package ru.pavlentygood.cellcapture.party.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.party.domain.DicePair
import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.PlayerId
import ru.pavlentygood.cellcapture.party.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class RollUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty
) {
    operator fun invoke(playerId: PlayerId): Either<Error, DicePair> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.roll(playerId)
                    .mapLeft { it.toUseCaseError() }
                    .onRight { saveParty(party) }
            } ?: PlayerNotFound.left()

    private fun Party.Roll.toUseCaseError() =
        when (this) {
            Party.PlayerNotCurrent -> PlayerNotCurrent
            Party.DicesAlreadyRolled -> DicesAlreadyRolled
        }

    sealed interface Error
    data object PlayerNotFound : Error
    data object PlayerNotCurrent : Error
    data object DicesAlreadyRolled : Error
}

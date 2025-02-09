package ru.pavlentygood.cellcapture.game.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class RollUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty
) {
    operator fun invoke(playerId: PlayerId): Either<Error, RolledDices> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.roll(playerId)
                    .mapLeft { it.toUseCaseError() }
                    .onRight { saveParty(party) }
            } ?: PlayerNotFound.left()

    private fun RollDicesError.toUseCaseError() =
        when (this) {
            ru.pavlentygood.cellcapture.game.domain.PlayerNotCurrent -> PlayerNotCurrent
            ru.pavlentygood.cellcapture.game.domain.DicesAlreadyRolled -> DicesAlreadyRolled
            PartyCompleted -> PartyAlreadyCompleted
        }

    sealed interface Error
    data object PlayerNotFound : Error
    data object PlayerNotCurrent : Error
    data object DicesAlreadyRolled : Error
    data object PartyAlreadyCompleted : Error
}

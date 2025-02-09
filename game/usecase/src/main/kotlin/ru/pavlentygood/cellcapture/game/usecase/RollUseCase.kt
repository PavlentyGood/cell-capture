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
    operator fun invoke(playerId: PlayerId): Either<RollUseCaseError, RolledDices> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.roll(playerId)
                    .mapLeft { it.toUseCaseError() }
                    .onRight { saveParty(party) }
            } ?: RollUseCaseError.PlayerNotFound.left()

    private fun RollDicesError.toUseCaseError() =
        when (this) {
            PlayerNotCurrent -> RollUseCaseError.PlayerNotCurrent
            DicesAlreadyRolled -> RollUseCaseError.DicesAlreadyRolled
            PartyCompleted -> RollUseCaseError.PartyCompleted
        }
}

sealed interface RollUseCaseError {
    data object PlayerNotFound : RollUseCaseError
    data object PlayerNotCurrent : RollUseCaseError
    data object DicesAlreadyRolled : RollUseCaseError
    data object PartyCompleted : RollUseCaseError
}

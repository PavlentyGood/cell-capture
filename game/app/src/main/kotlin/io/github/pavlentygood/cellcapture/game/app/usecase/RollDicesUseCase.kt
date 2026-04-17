package io.github.pavlentygood.cellcapture.game.app.usecase

import arrow.core.Either
import arrow.core.left
import io.github.pavlentygood.cellcapture.game.domain.*
import io.github.pavlentygood.cellcapture.game.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.game.app.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId

class RollDicesUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty
) {
    operator fun invoke(playerId: PlayerId): Either<RollDicesUseCaseError, RolledDices> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.roll(playerId)
                    .mapLeft { it.toUseCaseError() }
                    .onRight { saveParty(party) }
            } ?: RollDicesUseCaseError.PlayerNotFound.left()

    private fun RollDicesError.toUseCaseError() =
        when (this) {
            PlayerNotCurrent -> RollDicesUseCaseError.PlayerNotCurrent
            DicesAlreadyRolled -> RollDicesUseCaseError.DicesAlreadyRolled
            PartyCompleted -> RollDicesUseCaseError.PartyCompleted
        }
}

sealed interface RollDicesUseCaseError {
    data object PlayerNotFound : RollDicesUseCaseError
    data object PlayerNotCurrent : RollDicesUseCaseError
    data object DicesAlreadyRolled : RollDicesUseCaseError
    data object PartyCompleted : RollDicesUseCaseError
}

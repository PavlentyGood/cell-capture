package io.github.pavlentygood.cellcapture.game.usecase

import arrow.core.Either
import arrow.core.left
import io.github.pavlentygood.cellcapture.game.domain.*
import io.github.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.game.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId

class CaptureCellsUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty
) {
    operator fun invoke(playerId: PlayerId, area: Area): Either<CaptureCellsUseCaseError, Unit> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.capture(playerId, area)
                    .mapLeft { it.toUseCaseError() }
                    .onRight { saveParty(party) }
            } ?: CaptureCellsUseCaseError.PlayerNotFound.left()

    private fun CaptureCellsError.toUseCaseError() =
        when (this) {
            PlayerNotCurrent -> CaptureCellsUseCaseError.PlayerNotCurrent
            DicesNotRolled -> CaptureCellsUseCaseError.DicesNotRolled
            MismatchedArea -> CaptureCellsUseCaseError.MismatchedArea
            InaccessibleArea -> CaptureCellsUseCaseError.InaccessibleArea
            PartyCompleted -> CaptureCellsUseCaseError.PartyCompleted
        }
}

sealed interface CaptureCellsUseCaseError {
    data object PlayerNotFound : CaptureCellsUseCaseError
    data object PlayerNotCurrent : CaptureCellsUseCaseError
    data object DicesNotRolled : CaptureCellsUseCaseError
    data object MismatchedArea : CaptureCellsUseCaseError
    data object InaccessibleArea : CaptureCellsUseCaseError
    data object PartyCompleted : CaptureCellsUseCaseError
}

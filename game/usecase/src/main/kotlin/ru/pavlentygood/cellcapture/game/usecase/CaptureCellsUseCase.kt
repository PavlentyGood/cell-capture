package ru.pavlentygood.cellcapture.game.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class CaptureCellsUseCase(
    private val getPartyByPlayer: GetPartyByPlayer,
    private val saveParty: SaveParty
) {
    operator fun invoke(playerId: PlayerId, area: Area): Either<Error, Unit> =
        getPartyByPlayer(playerId)
            ?.let { party ->
                party.capture(playerId, area)
                    .mapLeft { it.toUseCaseError() }
                    .onRight { saveParty(party) }
            } ?: PlayerNotFound.left()

    private fun CaptureCellsError.toUseCaseError() =
        when (this) {
            ru.pavlentygood.cellcapture.game.domain.PlayerNotCurrent -> PlayerNotCurrent
            ru.pavlentygood.cellcapture.game.domain.DicesNotRolled -> DicesNotRolled
            ru.pavlentygood.cellcapture.game.domain.MismatchedArea -> MismatchedArea
            ru.pavlentygood.cellcapture.game.domain.InaccessibleArea -> InaccessibleArea
            PartyCompleted -> PartyAlreadyCompleted
        }

    sealed interface Error
    data object PlayerNotFound : Error
    data object PlayerNotCurrent : Error
    data object DicesNotRolled : Error
    data object MismatchedArea : Error
    data object InaccessibleArea : Error
    data object PartyAlreadyCompleted : Error
}

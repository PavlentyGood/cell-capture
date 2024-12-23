package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.domain.Area
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PlayerId

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

    private fun Party.Capture.toUseCaseError() =
        when (this) {
            Party.PlayerNotCurrent -> PlayerNotCurrent
            Party.DicesNotRolled -> DicesNotRolled
            Party.MismatchedArea -> MismatchedArea
            Party.InaccessibleArea -> InaccessibleArea
        }

    sealed interface Error
    data object PlayerNotFound : Error
    data object PlayerNotCurrent : Error
    data object DicesNotRolled : Error
    data object MismatchedArea : Error
    data object InaccessibleArea : Error
}

package ru.pavlentygood.cellcapture.game.usecase

import arrow.core.Either
import arrow.core.left
import ru.pavlentygood.cellcapture.game.domain.DicePair
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

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

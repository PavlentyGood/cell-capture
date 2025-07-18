package ru.pavlentygood.cellcapture.game.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class GetPartyByPlayerUseCase(
    private val getPartyByPlayer: GetPartyByPlayer
) {
    operator fun invoke(playerId: PlayerId): Either<GetPartyUseCaseError, Party> =
        getPartyByPlayer(playerId)?.right()
            ?: PartyNotFoundUseCaseError.left()
}

sealed interface GetPartyUseCaseError {
    data object PartyNotFoundUseCaseError : GetPartyUseCaseError
}

package io.github.pavlentygood.cellcapture.game.app.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.pavlentygood.cellcapture.game.app.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import io.github.pavlentygood.cellcapture.game.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.game.domain.Party
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId

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

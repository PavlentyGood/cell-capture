package io.github.pavlentygood.cellcapture.lobby.app.usecase

import arrow.core.Either
import arrow.core.left
import arrow.core.right
import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.lobby.app.usecase.GetPartyUseCaseError.PartyNotFoundUseCaseError
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.GetParty
import io.github.pavlentygood.cellcapture.lobby.domain.Party

class GetPartyUseCase(
    private val getParty: GetParty
) {
    operator fun invoke(partyId: PartyId): Either<GetPartyUseCaseError, Party> =
        getParty(partyId)?.right()
            ?: PartyNotFoundUseCaseError.left()
}

sealed interface GetPartyUseCaseError {
    data object PartyNotFoundUseCaseError : GetPartyUseCaseError
}

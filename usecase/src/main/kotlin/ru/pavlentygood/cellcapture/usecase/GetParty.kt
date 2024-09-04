package ru.pavlentygood.cellcapture.usecase

import arrow.core.Either
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId

fun interface GetParty {
    operator fun invoke(partyId: PartyId): Either<PartyNotFoundUseCaseError, Party>
}

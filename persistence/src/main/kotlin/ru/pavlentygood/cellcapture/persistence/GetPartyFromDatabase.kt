package ru.pavlentygood.cellcapture.persistence

import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.GetParty
import ru.pavlentygood.cellcapture.usecase.PartyNotFoundUseCaseError

class GetPartyFromDatabase(
    private val parties: Map<PartyId, Party>
) : GetParty {
    override fun invoke(partyId: PartyId) =
        parties[partyId]
            ?.right()
            ?: PartyNotFoundUseCaseError.left()
}

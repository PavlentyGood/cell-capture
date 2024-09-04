package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.GetParty

class GetPartyFromDatabase(
) : GetParty {
    override fun invoke(partyId: PartyId) = TODO("Not yet implemented")
}

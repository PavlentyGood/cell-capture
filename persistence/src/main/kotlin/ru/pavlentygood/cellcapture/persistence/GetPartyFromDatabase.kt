package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.GetParty

class GetPartyFromDatabase(
    val parties: Map<PartyId, Party>
) : GetParty {
    override fun invoke(partyId: PartyId): Party? =
        parties[partyId]
}

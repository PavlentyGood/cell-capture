package ru.pavlentygood.cellcapture.party.persistence

import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.PartyId
import ru.pavlentygood.cellcapture.party.usecase.port.GetParty

class GetPartyFromDatabase(
    val parties: Map<PartyId, Party>
) : GetParty {
    override fun invoke(partyId: PartyId): Party? =
        parties[partyId]
}

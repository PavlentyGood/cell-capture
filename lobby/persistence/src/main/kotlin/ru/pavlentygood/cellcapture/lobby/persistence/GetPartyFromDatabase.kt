package ru.pavlentygood.cellcapture.lobby.persistence

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty

class GetPartyFromDatabase(
    val parties: Map<PartyId, Party>
) : GetParty {
    override fun invoke(partyId: PartyId): Party? =
        parties[partyId]
}

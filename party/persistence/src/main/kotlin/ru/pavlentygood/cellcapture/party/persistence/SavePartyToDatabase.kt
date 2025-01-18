package ru.pavlentygood.cellcapture.party.persistence

import ru.pavlentygood.cellcapture.party.domain.Party
import ru.pavlentygood.cellcapture.party.domain.PartyId
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class SavePartyToDatabase(
    val parties: MutableMap<PartyId, Party> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: Party) {
        parties[party.id] = party
    }
}

package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.port.SaveParty

class SavePartyToDatabase(
    val parties: MutableMap<PartyId, Party> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: Party) {
        parties[party.id] = party
    }
}

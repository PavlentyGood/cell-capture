package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.SaveParty

class SavePartyToDatabase(
    val store: MutableMap<PartyId, Party> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: Party) {
        store[party.id] = party
    }
}

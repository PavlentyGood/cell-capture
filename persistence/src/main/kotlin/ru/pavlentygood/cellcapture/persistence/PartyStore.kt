package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.usecase.SaveParty

class PartyStore(
    val store: MutableMap<PartyId, Party> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: Party) {
        store[party.id] = party
    }
}

package ru.pavlentygood.cellcapture.lobby.persistence

import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PartyId
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

class SavePartyToDatabase(
    val parties: MutableMap<PartyId, Party> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: Party) {
        parties[party.id] = party
    }
}

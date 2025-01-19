package ru.pavlentygood.cellcapture.game.persistence

import ru.pavlentygood.cellcapture.game.domain.Party
import ru.pavlentygood.cellcapture.game.domain.PartyId
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

class SavePartyToDatabase(
    val parties: MutableMap<PartyId, Party> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: Party) {
        parties[party.id] = party
    }
}

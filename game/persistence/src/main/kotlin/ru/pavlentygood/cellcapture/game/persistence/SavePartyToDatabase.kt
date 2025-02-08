package ru.pavlentygood.cellcapture.game.persistence

import ru.pavlentygood.cellcapture.game.domain.AbstractParty
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty
import ru.pavlentygood.cellcapture.kernel.domain.PartyId

class SavePartyToDatabase(
    val parties: MutableMap<PartyId, AbstractParty> = mutableMapOf()
) : SaveParty {
    override fun invoke(party: AbstractParty) {
        parties[party.id] = party
    }
}

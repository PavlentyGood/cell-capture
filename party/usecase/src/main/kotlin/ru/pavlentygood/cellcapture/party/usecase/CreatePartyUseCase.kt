package ru.pavlentygood.cellcapture.party.usecase

import ru.pavlentygood.cellcapture.party.domain.PartyFactory
import ru.pavlentygood.cellcapture.party.domain.PartyInfo
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class CreatePartyUseCase(
    private val partyFactory: PartyFactory,
    private val saveParty: SaveParty
) {
    operator fun invoke(partyInfo: PartyInfo): Unit =
        saveParty(partyFactory.create(partyInfo))
}

package ru.pavlentygood.cellcapture.game.usecase

import ru.pavlentygood.cellcapture.game.domain.PartyFactory
import ru.pavlentygood.cellcapture.game.domain.PartyInfo
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

class CreatePartyUseCase(
    private val partyFactory: PartyFactory,
    private val saveParty: SaveParty
) {
    operator fun invoke(partyInfo: PartyInfo): Unit =
        saveParty(partyFactory.create(partyInfo))
}

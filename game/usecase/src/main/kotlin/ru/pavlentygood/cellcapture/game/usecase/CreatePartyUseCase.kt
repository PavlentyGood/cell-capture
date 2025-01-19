package ru.pavlentygood.cellcapture.game.usecase

import ru.pavlentygood.cellcapture.game.domain.CreateParty
import ru.pavlentygood.cellcapture.game.domain.PartyInfo
import ru.pavlentygood.cellcapture.game.usecase.port.SaveParty

class CreatePartyUseCase(
    private val createParty: CreateParty,
    private val saveParty: SaveParty
) {
    operator fun invoke(partyInfo: PartyInfo): Unit =
        saveParty(createParty(partyInfo))
}

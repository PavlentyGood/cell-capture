package ru.pavlentygood.cellcapture.usecase

import ru.pavlentygood.cellcapture.domain.PartyFactory

class CreateParty(
    private val partyFactory: PartyFactory,
    private val saveParty: SaveParty
) {
    operator fun invoke() =
        partyFactory.create()
            .also { saveParty(it) }
            .id
}

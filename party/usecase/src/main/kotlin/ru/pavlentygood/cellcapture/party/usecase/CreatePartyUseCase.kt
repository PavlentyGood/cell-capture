package ru.pavlentygood.cellcapture.party.usecase

import ru.pavlentygood.cellcapture.party.domain.*
import ru.pavlentygood.cellcapture.party.usecase.port.SaveParty

class CreatePartyUseCase(
    private val partyFactory: PartyFactory,
    private val saveParty: SaveParty
) {
    operator fun invoke(ownerName: PlayerName) =
        partyFactory.create(ownerName)
            .also { saveParty(it) }
            .toResult()
}

data class CreatePartyResult(
    val partyId: PartyId,
    val ownerId: PlayerId
)

fun Party.toResult() =
    CreatePartyResult(
        partyId = id,
        ownerId = ownerId
    )

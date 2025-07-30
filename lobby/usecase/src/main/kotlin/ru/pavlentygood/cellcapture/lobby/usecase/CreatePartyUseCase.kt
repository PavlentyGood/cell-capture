package ru.pavlentygood.cellcapture.lobby.usecase

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerName
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PartyFactory
import ru.pavlentygood.cellcapture.lobby.usecase.port.SaveParty

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

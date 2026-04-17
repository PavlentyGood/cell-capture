package io.github.pavlentygood.cellcapture.lobby.app.usecase

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.kernel.domain.PlayerName
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.SaveParty
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.domain.PartyFactory

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

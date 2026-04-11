package io.github.pavlentygood.cellcapture.lobby.app.output.db

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.GetParty

class GetPartyFromDatabase(
    private val partyRepository: PartyRepository
) : GetParty {

    override fun invoke(partyId: PartyId): Party? =
        partyRepository.findById(partyId.toUUID())
            .map { mapPartyToDomain(it) }
            .orElse(null)
}

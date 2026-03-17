package io.github.pavlentygood.cellcapture.lobby.persistence

import io.github.pavlentygood.cellcapture.kernel.domain.PartyId
import io.github.pavlentygood.cellcapture.lobby.domain.Party
import io.github.pavlentygood.cellcapture.lobby.usecase.port.GetParty

class GetPartyFromDatabase(
    private val partyRepository: PartyRepository
) : GetParty {

    override fun invoke(partyId: PartyId): Party? =
        partyRepository.findById(partyId.toUUID())
            .map { mapPartyToDomain(it) }
            .orElse(null)
}

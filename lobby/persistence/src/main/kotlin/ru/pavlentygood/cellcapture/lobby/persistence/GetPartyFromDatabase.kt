package ru.pavlentygood.cellcapture.lobby.persistence

import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetParty

class GetPartyFromDatabase(
    private val partyRepository: PartyRepository,
    private val mapPartyToDomain: MapPartyToDomain
) : GetParty {

    override fun invoke(partyId: PartyId): Party? =
        partyRepository.findById(partyId.toUUID())
            .map { mapPartyToDomain(it) }
            .orElse(null)
}

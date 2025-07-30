package ru.pavlentygood.cellcapture.lobby.persistence

import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer

class GetPartyByPlayerFromDatabase(
    private val partyRepository: PartyRepository,
    private val mapPartyToDomain: MapPartyToDomain
) : GetPartyByPlayer {

    override operator fun invoke(playerId: PlayerId): Party? =
        partyRepository.getByPlayersId(playerId.toInt())
            .map { mapPartyToDomain(it) }
            .orElse(null)
}

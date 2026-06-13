package io.github.pavlentygood.cellcapture.lobby.app.output.db

import io.github.pavlentygood.cellcapture.kernel.domain.PlayerId
import io.github.pavlentygood.cellcapture.lobby.app.usecase.port.GetPartyByPlayer
import io.github.pavlentygood.cellcapture.lobby.domain.Party

class GetPartyByPlayerFromDatabase(
    private val partyRepository: PartyRepository
) : GetPartyByPlayer {

    override operator fun invoke(playerId: PlayerId): Party? =
        partyRepository.getByPlayer(playerId.toInt())
            .map { mapPartyToDomain(it) }
            .orElse(null)
}

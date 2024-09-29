package ru.pavlentygood.cellcapture.persistence

import ru.pavlentygood.cellcapture.domain.Party
import ru.pavlentygood.cellcapture.domain.PartyId
import ru.pavlentygood.cellcapture.domain.PlayerId
import ru.pavlentygood.cellcapture.usecase.GetPartyByPlayer

class GetPartyByPlayerFromDatabase(
    private val parties: Map<PartyId, Party>
) : GetPartyByPlayer {
    override operator fun invoke(playerId: PlayerId) =
        parties.values.find { party ->
            party.players.any { it.id == playerId }
        }
}

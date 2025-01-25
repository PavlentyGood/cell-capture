package ru.pavlentygood.cellcapture.lobby.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId
import ru.pavlentygood.cellcapture.lobby.domain.Party
import ru.pavlentygood.cellcapture.lobby.domain.PlayerLimit
import ru.pavlentygood.cellcapture.lobby.domain.RestoreParty
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer

class GetPartyByPlayerFromDatabase(
    private val parties: Map<PartyId, Party>,
    private val restoreParty: RestoreParty
) : GetPartyByPlayer {
    override operator fun invoke(playerId: PlayerId): Party? =
        parties.values.find { party ->
            party.getPlayers().any { it.id == playerId }
        }?.let { party ->
            val partyId = PartyId(party.id.toUUID())
            val started = party.started

            val playerLimit = PlayerLimit.from(party.playerLimit.value).getOrElse {
                error("Illegal player limit: ${party.playerLimit.value}")
            }

            val players = party.getPlayers()

            val ownerId = PlayerId(party.ownerId.toInt())

            restoreParty(
                id = partyId,
                started = started,
                playerLimit = playerLimit,
                players = players,
                ownerId = ownerId
            ).getOrElse {
                error("Restore party error: $it. #$partyId")
            }
        }
}

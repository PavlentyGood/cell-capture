package ru.pavlentygood.cellcapture.lobby.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.lobby.domain.*
import ru.pavlentygood.cellcapture.lobby.usecase.port.GetPartyByPlayer

class GetPartyByPlayerFromDatabase(
    private val parties: Map<PartyId, Party>,
    private val restoreParty: RestoreParty
) : GetPartyByPlayer {
    override operator fun invoke(playerId: PlayerId): Party? =
        parties.values.find { party ->
            party.players.any { it.id == playerId }
        }?.let { party ->
            val partyId = PartyId(party.id.toUUID())
            val started = party.started

            val playerLimit = PlayerLimit.from(party.playerLimit.value).getOrElse {
                error("Invalid player limit: ${party.playerLimit.value}")
            }

            val players = party.players.toList()

            val ownerId = PlayerId(party.ownerId.toInt())

            restoreParty(
                id = partyId,
                started = started,
                playerLimit = playerLimit,
                players = players,
                ownerId = ownerId
            )
        }
}

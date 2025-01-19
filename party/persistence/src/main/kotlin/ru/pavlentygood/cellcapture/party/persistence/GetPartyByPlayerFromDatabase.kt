package ru.pavlentygood.cellcapture.party.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.party.domain.*
import ru.pavlentygood.cellcapture.party.usecase.port.GetPartyByPlayer

class GetPartyByPlayerFromDatabase(
    val parties: MutableMap<PartyId, Party>,
    private val restoreParty: RestoreParty
) : GetPartyByPlayer {
    override operator fun invoke(playerId: PlayerId): Party? =
        parties.values.find { party ->
            party.players.any { it.id == playerId }
        }?.let { party ->
            val partyId = PartyId(party.id.toUUID())
            val completed = party.completed

            val dicePair = party.dicePair?.let { dicePair ->
                DicePair(
                    first = Dice.from(dicePair.first.value).getOrElse {
                        error("Invalid dice value: ${dicePair.first.value}")
                    },
                    second = Dice.from(dicePair.second.value).getOrElse {
                        error("Invalid dice value: ${dicePair.first.value}")
                    }
                )
            }

            val cells = party.getCells()

            val players = party.players.toList()

            val currentPlayerId = PlayerId(party.currentPlayerId.toInt())

            val ownerId = PlayerId(party.ownerId.toInt())

            restoreParty(
                id = partyId,
                completed = completed,
                dicePair = dicePair,
                cells = cells,
                players = players,
                currentPlayerId = currentPlayerId,
                ownerId = ownerId
            ).getOrElse {
                error("Invalid player queue in party #${party.id}")
            }
        }
}

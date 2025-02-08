package ru.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class GetPartyByPlayerFromDatabase(
    val parties: MutableMap<PartyId, AbstractParty>,
    private val restoreParty: RestoreParty
) : GetPartyByPlayer {
    override operator fun invoke(playerId: PlayerId): AbstractParty? =
        parties.values.find { party ->
            party.getPlayers().any { it.id == playerId }
        }?.let { party ->
            val partyId = PartyId(party.id.toUUID())
            val completed = party is CompletedParty

            val dices = party.dices.let { d ->
                if (d is RolledDices) {
                    RolledDices(
                        first = Dice.from(d.first.value).getOrElse {
                            error("Invalid dice value: ${d.first.value}")
                        },
                        second = Dice.from(d.second.value).getOrElse {
                            error("Invalid dice value: ${d.first.value}")
                        }
                    )
                } else {
                    Dices.notRolled()
                }
            }

            val cells = party.getCells()

            val players = party.getPlayers()

            val currentPlayerId = PlayerId(party.currentPlayerId.toInt())

            val ownerId = PlayerId(party.ownerId.toInt())

            restoreParty(
                id = partyId,
                completed = completed,
                dices = dices,
                cells = cells,
                players = players,
                currentPlayerId = currentPlayerId,
                ownerId = ownerId
            ).getOrElse {
                error("Restore party error: $it. #$partyId")
            }
        }
}

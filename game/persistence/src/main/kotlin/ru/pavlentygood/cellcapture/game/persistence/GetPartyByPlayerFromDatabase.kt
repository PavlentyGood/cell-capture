package ru.pavlentygood.cellcapture.game.persistence

import arrow.core.getOrElse
import ru.pavlentygood.cellcapture.game.domain.*
import ru.pavlentygood.cellcapture.game.usecase.port.GetPartyByPlayer
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class GetPartyByPlayerFromDatabase(
    val parties: MutableMap<PartyId, Party>,
    private val restoreParty: RestoreParty
) : GetPartyByPlayer {
    override operator fun invoke(playerId: PlayerId): Party? =
        parties.values.find { party ->
            party.players.any { it.id == playerId }
        }?.let { party ->
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

            restoreParty(
                id = PartyId(party.id.toUUID()),
                completed = party is CompletedParty,
                dices = dices,
                cells = party.cells,
                players = party.players,
                currentPlayerId = party.currentPlayerId,
                ownerId = party.ownerId
            ).getOrElse {
                error("Restore party error: $it. #${party.id}")
            }
        }
}

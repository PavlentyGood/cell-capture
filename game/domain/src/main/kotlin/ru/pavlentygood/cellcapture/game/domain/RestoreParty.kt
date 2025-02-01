package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.flatMap
import arrow.core.left
import arrow.core.right
import ru.pavlentygood.cellcapture.kernel.domain.PartyId
import ru.pavlentygood.cellcapture.kernel.domain.Player
import ru.pavlentygood.cellcapture.kernel.domain.PlayerId

class RestoreParty {

    operator fun invoke(
        id: PartyId,
        completed: Boolean,
        dicePair: DicePair?,
        cells: Array<Array<PlayerId>>,
        players: List<Player>,
        currentPlayerId: PlayerId,
        ownerId: PlayerId
    ): Either<Any, Party> =
        PlayerList.from(
            ownerId = ownerId,
            players = players
        ).flatMap { playerList ->
            if (playerList.playerIds.contains(currentPlayerId)) {
                Party(
                    id = id,
                    completed = completed,
                    dicePair = dicePair,
                    field = Field(
                        cells = cells
                    ),
                    ownerId = ownerId,
                    currentPlayerId = currentPlayerId,
                    players = playerList.players
                ).right()
            } else {
                IllegalCurrentPlayerId.left()
            }
        }
}

data object IllegalCurrentPlayerId

package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either
import arrow.core.flatMap
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
            PlayerQueue.restore(
                playerList = playerList,
                currentPlayerId = currentPlayerId
            ).map { playerQueue ->
                Party(
                    id = id,
                    completed = completed,
                    dicePair = dicePair,
                    field = Field(
                        cells = cells
                    ),
                    playerQueue = playerQueue,
                    ownerId = ownerId
                )
            }
        }
}

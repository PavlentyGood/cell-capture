package ru.pavlentygood.cellcapture.game.domain

import arrow.core.Either

class RestoreParty {

    operator fun invoke(
        id: PartyId,
        completed: Boolean,
        dicePair: DicePair?,
        cells: Array<Array<PlayerId>>,
        players: List<Player>,
        currentPlayerId: PlayerId,
        ownerId: PlayerId
    ): Either<PlayerQueue.InvalidPlayerQueue, Party> =
        PlayerQueue.restore(
            players = players,
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
